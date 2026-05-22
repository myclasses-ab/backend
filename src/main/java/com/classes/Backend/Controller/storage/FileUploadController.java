package com.classes.Backend.Controller.storage;

import com.classes.Backend.Service.storage.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    private final S3Service S3_SERVICE;

    private static final String FOLDER_INSTITUTE_LOGO = "instituteLogo";
    private static final String FOLDER_INSTITUTE_BANNER = "instituteBanner";
    private static final String FOLDER_FACULTY_IMAGE = "facultyImage";

    /**
     * Upload or replace institute logo.
     * Uses deterministic naming: instituteLogo/{instituteIdentifier}_logo.{ext}
     * If a logo already exists for this institute, it will be replaced.
     */
    @PostMapping("/logo")
    public ResponseEntity<?> uploadLogo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("instituteIdentifier") String instituteIdentifier,
            @RequestParam(value = "oldLogoUrl", required = false) String oldLogoUrl
    ) {
        log.info("Upload logo request. instituteIdentifier={}, fileName={}, fileSize={}, hasOldUrl={}",
                instituteIdentifier, file.getOriginalFilename(), file.getSize(), oldLogoUrl != null);
        
        // Delete old logo if provided - this ensures clean replacement
        if (oldLogoUrl != null && !oldLogoUrl.isEmpty()) {
            try {
                S3_SERVICE.deleteFile(oldLogoUrl);
                log.info("Deleted old logo: {}", oldLogoUrl);
            } catch (Exception e) {
                log.warn("Failed to delete old logo (may not exist): {}", oldLogoUrl);
            }
        }
        
        return handleUpload(file, FOLDER_INSTITUTE_LOGO, instituteIdentifier);
    }

    /**
     * Upload or replace institute banner.
     * Uses deterministic naming: instituteBanner/{instituteIdentifier}_banner.{ext}
     * If a banner already exists for this institute, it will be replaced.
     */
    @PostMapping("/banner")
    public ResponseEntity<?> uploadBanner(
            @RequestParam("file") MultipartFile file,
            @RequestParam("instituteIdentifier") String instituteIdentifier,
            @RequestParam(value = "oldBannerUrl", required = false) String oldBannerUrl
    ) {
        log.info("Upload banner request. instituteIdentifier={}, fileName={}, fileSize={}, hasOldUrl={}",
                instituteIdentifier, file.getOriginalFilename(), file.getSize(), oldBannerUrl != null);
        
        // Delete old banner if provided - this ensures clean replacement
        if (oldBannerUrl != null && !oldBannerUrl.isEmpty()) {
            try {
                S3_SERVICE.deleteFile(oldBannerUrl);
                log.info("Deleted old banner: {}", oldBannerUrl);
            } catch (Exception e) {
                log.warn("Failed to delete old banner (may not exist): {}", oldBannerUrl);
            }
        }
        
        return handleUpload(file, FOLDER_INSTITUTE_BANNER, instituteIdentifier);
    }

    /**
     * Upload or replace faculty image.
     * Uses deterministic naming: facultyImage/{facultyIdentifier}_image.{ext}
     * If an image already exists for this faculty, it will be replaced.
     */
    @PostMapping("/faculty")
    public ResponseEntity<?> uploadFacultyImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("facultyIdentifier") String facultyIdentifier,
            @RequestParam(value = "oldImageUrl", required = false) String oldImageUrl
    ) {
        log.info("Upload faculty image request. facultyIdentifier={}, fileName={}, fileSize={}, hasOldUrl={}",
                facultyIdentifier, file.getOriginalFilename(), file.getSize(), oldImageUrl != null);
        
        // Delete old image if provided - this ensures clean replacement
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            try {
                S3_SERVICE.deleteFile(oldImageUrl);
                log.info("Deleted old faculty image: {}", oldImageUrl);
            } catch (Exception e) {
                log.warn("Failed to delete old faculty image (may not exist): {}", oldImageUrl);
            }
        }
        
        return handleUpload(file, FOLDER_FACULTY_IMAGE, facultyIdentifier);
    }

    /**
     * Generic file upload endpoint.
     * For resource-specific uploads (logo/banner/faculty), use the dedicated endpoints above.
     */
    @PostMapping
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder,
            @RequestParam(value = "resourceId", required = false) String resourceId,
            @RequestParam(value = "oldFileUrl", required = false) String oldFileUrl
    ) {
        log.info("Generic upload request. folder={}, resourceId={}, fileName={}, fileSize={}, hasOldUrl={}",
                folder, resourceId, file.getOriginalFilename(), file.getSize(), oldFileUrl != null);
        
        // Delete old file if provided
        if (oldFileUrl != null && !oldFileUrl.isEmpty()) {
            try {
                S3_SERVICE.deleteFile(oldFileUrl);
                log.info("Deleted old file: {}", oldFileUrl);
            } catch (Exception e) {
                log.warn("Failed to delete old file (may not exist): {}", oldFileUrl);
            }
        }
        
        return handleUpload(file, folder, resourceId);
    }

    /**
     * Delete a file from S3 by its URL or object key.
     * Accepts both full URLs (https://...) and object keys (folder/filename.ext)
     */
    @DeleteMapping
    public ResponseEntity<?> deleteFile(@RequestParam("url") String fileUrlOrKey) {
        log.info("Delete request received. urlOrKey={}", fileUrlOrKey);
        try {
            // Handle both full URLs and object keys
            String objectKey = fileUrlOrKey;
            if (fileUrlOrKey.startsWith("http://") || fileUrlOrKey.startsWith("https://")) {
                // Extract key from full URL
                String extracted = S3_SERVICE.extractObjectKey(fileUrlOrKey);
                if (extracted != null) {
                    objectKey = extracted;
                }
            }
            S3_SERVICE.deleteFileByKey(objectKey);
            log.info("Delete successful. key={}", objectKey);
            return ResponseEntity.ok(Map.of("message", "File deleted successfully", "key", objectKey));
        } catch (Exception e) {
            log.error("Delete failed. urlOrKey={}, error={}", fileUrlOrKey, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete file: " + e.getMessage()));
        }
    }

    /**
     * Get the expected S3 URL for a resource without uploading.
     * Useful for checking if a file exists or getting the deterministic URL.
     */
    @GetMapping("/url")
    public ResponseEntity<?> getExpectedUrl(
            @RequestParam("folder") String folder,
            @RequestParam("resourceId") String resourceId,
            @RequestParam(value = "extension", required = false, defaultValue = ".png") String extension
    ) {
        String objectKey;
        String suffix = getSuffixForFolder(folder);
        if (suffix != null) {
            objectKey = folder + "/" + resourceId + "_" + suffix + extension;
        } else {
            objectKey = folder + "/" + resourceId + extension;
        }
        String url = S3_SERVICE.getFileUrl(objectKey);
        boolean exists = S3_SERVICE.doesObjectExist(objectKey);
        
        return ResponseEntity.ok(Map.of(
            "objectKey", objectKey,
            "url", url,
            "exists", exists
        ));
    }

    private ResponseEntity<?> handleUpload(MultipartFile file, String folder, String resourceId) {
        try {
            String objectKey = S3_SERVICE.uploadFile(file, folder, resourceId);
            String fullUrl = S3_SERVICE.getFileUrl(objectKey);
            Map<String, Object> response = new HashMap<>();
            response.put("key", objectKey);        // Object key (e.g., "instituteLogo/uuid_logo.png")
            response.put("url", fullUrl);          // Full S3 URL (for backward compatibility)
            response.put("fileName", file.getOriginalFilename());
            response.put("folder", folder);
            response.put("resourceId", resourceId);
            log.info("Upload successful. folder={}, resourceId={}, key={}", folder, resourceId, objectKey);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Upload validation failed. folder={}, resourceId={}, error={}", folder, resourceId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Upload IO error. folder={}, resourceId={}, error={}", folder, resourceId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload file: " + e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Upload runtime error. folder={}, resourceId={}, error={}", folder, resourceId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String getSuffixForFolder(String folder) {
        return switch (folder) {
            case "instituteLogo" -> "logo";
            case "instituteBanner" -> "banner";
            case "facultyImage" -> "image";
            default -> null;
        };
    }
}
