package com.classes.Backend.Service.messagecentral;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for sending and verifying OTPs via Message Central Verify Now API.
 * <p>
 * Docs: https://cpaas.messagecentral.com
 * Flow:
 * 1. Generate auth token ({@code /auth/v1/authentication/token})
 * 2. Send OTP ({@code POST /verification/v3/send})
 * 3. Validate OTP ({@code GET /verification/v3/validateOtp})
 */
@Slf4j
@Service
public class MessageCentralService {

    private final RestTemplate restTemplate = createRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static RestTemplate createRestTemplate() {
        RestTemplate template = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            log.info("MC REQUEST {} {}", request.getMethod(), request.getURI());
            log.info("MC REQUEST headers: {}", request.getHeaders());
            return execution.execute(request, body);
        });
        template.setInterceptors(interceptors);
        return template;
    }

    @Value("${messagecentral.base-url:https://cpaas.messagecentral.com}")
    private String baseUrl;

    @Value("${messagecentral.customer-id}")
    private String customerId;

    @Value("${messagecentral.key}")
    private String key;

    @Value("${messagecentral.country-code:91}")
    private String countryCode;

    @Value("${messagecentral.flow-type:SMS}")
    private String flowType;

    // Cache verification IDs per mobile number so verifyOtp can use the right one.
    private final Map<String, String> verificationIdCache = new ConcurrentHashMap<>();

    /**
     * Generate a fresh auth token from Message Central.
     */
    public String getAuthToken() {
        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/auth/v1/authentication/token")
                    .queryParam("customerId", customerId)
                    .queryParam("key", key)
                    .queryParam("scope", "NEW")
                    .queryParam("country", countryCode)
                    .toUriString();

            log.info("Requesting Message Central auth token for customerId={}", customerId);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

            String responseBody = response.getBody();
            log.info("Message Central token response: {}", responseBody);

            JsonNode root = objectMapper.readTree(responseBody);
            int responseCode = root.path("status").asInt(root.path("responseCode").asInt(0));

            // Message Central may return the JWT in several possible fields.
            String token = root.path("token").asText(null);
            if (token == null || token.isBlank()) {
                token = root.path("authToken").asText(null);
            }
            if (token == null || token.isBlank()) {
                token = root.path("data").path("token").asText(null);
            }
            if (token == null || token.isBlank()) {
                token = root.path("data").path("authToken").asText(null);
            }

            if (responseCode != 200) {
                throw new MessageCentralException(
                        "Message Central token request failed: " + responseCode + ", Response: " + responseBody);
            }

            if (token == null || token.isBlank()) {
                throw new MessageCentralException(
                        "Auth token missing in Message Central response. Response: " + responseBody);
            }

            return token;
        } catch (RestClientException e) {
            log.error("Failed to generate Message Central auth token", e);
            throw new MessageCentralException("Unable to generate OTP auth token: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while generating Message Central auth token", e);
            throw new MessageCentralException("Unable to generate OTP auth token", e);
        }
    }

    /**
     * Send an OTP to the given mobile number.
     *
     * @param mobileNumber mobile number without country prefix (e.g. 9999999999)
     * @return verificationId from Message Central
     */
    public String sendOtp(String mobileNumber) {
        String authToken = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("authToken", authToken);

        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/verification/v3/send")
                    .queryParam("customerId", customerId)
                    .queryParam("countryCode", countryCode)
                    .queryParam("flowType", flowType)
                    .queryParam("type", "OTP")
                    .queryParam("mobileNumber", mobileNumber)
                    .queryParam("otpLength", "4")
                    .toUriString();

            log.info("Sending OTP via Message Central to +{} {} URL={}", countryCode, mobileNumber, url);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(headers), String.class);

            String responseBody = response.getBody();
            log.info("Message Central send OTP response: {}", responseBody);

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode data = root.path("data");
            String verificationId = data.path("verificationId").asText(null);

            if (verificationId == null || verificationId.isBlank()) {
                throw new MessageCentralException("verificationId missing in send OTP response: " + responseBody);
            }

            verificationIdCache.put(mobileNumber, verificationId);
            return verificationId;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to send OTP via Message Central for +{} {}: status={}, body={}",
                    countryCode, mobileNumber, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new MessageCentralException("Failed to send OTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            log.error("Failed to send OTP via Message Central for +{} {}", countryCode, mobileNumber, e);
            throw new MessageCentralException("Failed to send OTP: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while sending OTP via Message Central", e);
            throw new MessageCentralException("Failed to send OTP", e);
        }
    }

    /**
     * Validate an OTP against Message Central.
     *
     * @param mobileNumber  mobile number without country prefix
     * @param otp           OTP entered by the user
     * @param verificationId optional verificationId; if null, uses the cached id for the number
     * @return true if verification completed successfully
     */
    public boolean verifyOtp(String mobileNumber, String otp, String verificationId) {
        String effectiveVerificationId = verificationId != null && !verificationId.isBlank()
                ? verificationId
                : verificationIdCache.get(mobileNumber);

        if (effectiveVerificationId == null || effectiveVerificationId.isBlank()) {
            throw new MessageCentralException("No verificationId found for this phone number. Please request OTP again.");
        }

        String authToken = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("authToken", authToken);

        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/verification/v3/validateOtp")
                    .queryParam("verificationId", effectiveVerificationId)
                    .queryParam("code", otp)
                    .queryParam("flowType", flowType)
                    .toUriString();

            log.info("Validating OTP via Message Central for +{} {} URL={}", countryCode, mobileNumber, url);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            String responseBody = response.getBody();
            log.info("Message Central validate OTP response: {}", responseBody);

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode data = root.path("data");
            int responseCode = root.path("responseCode").asInt(0);
            String responseMessage = root.path("message").asText("");
            String verificationStatus = data.path("verificationStatus").asText("");

            if (responseCode == 200 && "VERIFICATION_COMPLETED".equalsIgnoreCase(verificationStatus)) {
                verificationIdCache.remove(mobileNumber);
                return true;
            }

            String errorMessage = data.path("errorMessage").asText(null);
            if (errorMessage == null || errorMessage.isBlank()) {
                errorMessage = responseMessage != null && !responseMessage.isBlank()
                        ? responseMessage
                        : "OTP verification failed";
            }
            throw new MessageCentralException(errorMessage);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to validate OTP via Message Central for +{} {}: status={}, body={}",
                    countryCode, mobileNumber, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new MessageCentralException("Failed to validate OTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            log.error("Failed to validate OTP via Message Central for +{} {}", countryCode, mobileNumber, e);
            throw new MessageCentralException("Failed to validate OTP: " + e.getMessage(), e);
        } catch (MessageCentralException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while validating OTP via Message Central", e);
            throw new MessageCentralException("Failed to validate OTP", e);
        }
    }

    /**
     * Remove any cached verification id for the given mobile number.
     */
    public void clearVerificationId(String mobileNumber) {
        verificationIdCache.remove(mobileNumber);
    }
}
