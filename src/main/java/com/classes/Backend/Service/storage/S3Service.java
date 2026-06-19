package com.classes.Backend.Service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
public class S3Service {

    @Value("${aws.s3.region:ap-south-1}")
    private String awsS3Region;

    @Value("${aws.s3.bucket-name:myclassesimages}")
    private String bucketName;

    @Value("${aws.s3.access-key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret-key}")
    private String awsS3SecretKey;

    private S3Client s3Client;

    // Suffix mapping for different image types - ensures consistent naming
    private static final Map<String, String> FOLDER_SUFFIX_MAP = Map.of(
        "instituteLogo", "logo",
        "instituteBanner", "banner",
        "facultyImage", "image"
    );

    @PostConstruct
    public void init() {
        log.info("Initializing S3 client for bucket: {} in region: {}", bucketName, awsS3Region);
        this.s3Client = S3Client.builder()
                .region(Region.of(awsS3Region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsS3AccessKey, awsS3SecretKey)))
                .build();
    }

    @PreDestroy
    public void destroy() {
        if (this.s3Client != null) {
            log.info("Closing S3 client");
            this.s3Client.close();
        }
    }

    /**
     * Upload a file to S3 with deterministic naming based on resourceId and folder type.
     * If a file with the same key exists, it will be overwritten (S3 PUT behavior).
     * 
     * @param file The multipart file to upload
     * @param folder The folder/category (instituteLogo, instituteBanner, facultyImage)
     * @param resourceId The unique resource identifier (e.g., institute identifier)
     * @return The S3 object key (not full URL) of the uploaded file
     * @throws IOException if file processing fails
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        return uploadFile(file, folder, null);
    }

    /**
     * Upload a file to S3 with deterministic naming based on resourceId and folder type.
     * If a file with the same key exists, it will be overwritten (S3 PUT behavior).
     * 
     * @param file The multipart file to upload
     * @param folder The folder/category (instituteLogo, instituteBanner, facultyImage)
     * @param resourceId The unique resource identifier (e.g., institute identifier)
     * @return The S3 object key (not full URL) of the uploaded file
     * @throws IOException if file processing fails
     */
    public String uploadFile(MultipartFile file, String folder, String resourceId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }
        if (folder == null || folder.trim().isEmpty()) {
            throw new IllegalArgumentException("Folder must not be null or empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename);
        String objectKey = buildObjectKey(folder, resourceId, extension);
        
        log.info("Uploading file to S3. folder={}, resourceId={}, objectKey={}", folder, resourceId, objectKey);

        // Check if object already exists - this means user is replacing the image
        boolean exists = doesObjectExist(objectKey);
        if (exists) {
            log.info("Object already exists at key={}, will be overwritten", objectKey);
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("File uploaded successfully. objectKey: {}", objectKey);
            return objectKey;  // Return object key, not full URL
        } catch (S3Exception e) {
            String errorMessage = e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage();
            String errorCode = e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "Unknown";
            log.error("AWS S3 upload failed. folder={}, resourceId={}, errorMessage={}, errorCode={}",
                    folder, resourceId, errorMessage, errorCode, e);
            throw new RuntimeException("Failed to upload file to S3: " + errorMessage, e);
        }
    }

    /**
     * Upload from InputStream with deterministic naming.
     * If a file with the same key exists, it will be overwritten.
     * @return The S3 object key (not full URL)
     */
    public String uploadFile(InputStream inputStream, String contentType, long contentLength, String folder, String extension) {
        return uploadFile(inputStream, contentType, contentLength, folder, extension, null);
    }

    /**
     * Upload from InputStream with deterministic naming.
     * If a file with the same key exists, it will be overwritten.
     * @return The S3 object key (not full URL)
     */
    public String uploadFile(InputStream inputStream, String contentType, long contentLength, String folder, String extension, String resourceId) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }
        if (folder == null || folder.trim().isEmpty()) {
            throw new IllegalArgumentException("Folder must not be null or empty");
        }

        String objectKey = buildObjectKey(folder, resourceId, extension);
        log.info("Uploading stream to S3. folder={}, resourceId={}, objectKey={}", folder, resourceId, objectKey);

        // Check if object already exists
        boolean exists = doesObjectExist(objectKey);
        if (exists) {
            log.info("Object already exists at key={}, will be overwritten", objectKey);
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            log.info("Stream uploaded successfully. objectKey: {}", objectKey);
            return objectKey;  // Return object key, not full URL
        } catch (S3Exception e) {
            String errorMessage = e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage();
            String errorCode = e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "Unknown";
            log.error("AWS S3 upload failed. folder={}, resourceId={}, errorMessage={}, errorCode={}",
                    folder, resourceId, errorMessage, errorCode, e);
            throw new RuntimeException("Failed to upload file to S3: " + errorMessage, e);
        }
    }

    /**
     * Delete a file from S3 by its full URL.
     * Silently ignores if file doesn't exist or URL is invalid.
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;
        try {
            String objectKey = extractObjectKey(fileUrl);
            if (objectKey != null) {
                log.info("Deleting file from S3. objectKey={}", objectKey);
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();
                s3Client.deleteObject(deleteObjectRequest);
                log.info("File deleted successfully.");
            }
        } catch (S3Exception e) {
            String errorMessage = e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage();
            String errorCode = e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "Unknown";
            log.error("AWS S3 delete failed. fileUrl={}, errorMessage={}, errorCode={}",
                    fileUrl, errorMessage, errorCode, e);
            throw new RuntimeException("Failed to delete file from S3: " + errorMessage, e);
        }
    }

    /**
     * Delete a file from S3 by its object key.
     */
    public void deleteFileByKey(String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) return;
        try {
            log.info("Deleting file from S3 by key. objectKey={}", objectKey);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully.");
        } catch (S3Exception e) {
            String errorMessage = e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage();
            log.error("AWS S3 delete failed. objectKey={}, errorMessage={}", objectKey, errorMessage, e);
            throw new RuntimeException("Failed to delete file from S3: " + errorMessage, e);
        }
    }

    /**
     * Get the full public URL for an S3 object key.
     */
    public String getFileUrl(String objectKey) {
        return "https://" + bucketName + ".s3." + awsS3Region + ".amazonaws.com/" + objectKey;
    }

    /**
     * Extract the object key from a full S3 URL.
     */
    public String extractObjectKey(String fileUrl) {
        String prefix = "https://" + bucketName + ".s3." + awsS3Region + ".amazonaws.com/";
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }
        return null;
    }

    /**
     * Check if an object exists in S3.
     */
    public boolean doesObjectExist(String objectKey) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3Client.headObject(headRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            log.warn("Error checking object existence for key={}: {}", objectKey, e.getMessage());
            return false;
        }
    }

    /**
     * Build a deterministic object key based on folder type and resource identifier.
     * Format: {folder}/{resourceId}_{suffix}.{extension}
     * Example: instituteLogo/institute-123_logo.png
     */
    private String buildObjectKey(String folder, String resourceId, String extension) {
        if (resourceId != null && !resourceId.trim().isEmpty()) {
            String suffix = FOLDER_SUFFIX_MAP.get(folder);
            if (suffix != null) {
                // Ensure consistent naming: {folder}/{resourceId}_{suffix}.{ext}
                return folder + "/" + resourceId.trim() + "_" + suffix + extension;
            }
        }
        // Fallback to UUID for generic uploads without resourceId
        return folder + "/" + UUID.randomUUID() + extension;
    }

    /**
     * Extract file extension from original filename, preserving the original format.
     * Returns empty string if no extension found.
     */
    private String extractExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0 && lastDotIndex < originalFilename.length() - 1) {
                return originalFilename.substring(lastDotIndex);
            }
        }
        return "";
    }
}
