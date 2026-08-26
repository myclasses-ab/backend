package com.classes.Backend.Service.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class LocationResolverService {

    private static final Pattern COORDINATE_PATTERN = Pattern.compile("[-+]?\\d+\\.\\d+,\\s*[-+]?\\d+\\.\\d+");
    private static final Pattern DATA_LAT_PATTERN = Pattern.compile("!3d(-?\\d+\\.\\d+)");
    private static final Pattern DATA_LNG_PATTERN = Pattern.compile("!4d(-?\\d+\\.\\d+)");

    private final RestTemplate restTemplate;
    private final String unshortenMeToken;

    public LocationResolverService(
            @Value("${unshorten.me.token:}") String unshortenMeToken) {
        this.restTemplate = new RestTemplate();
        this.unshortenMeToken = unshortenMeToken;
    }

    public Optional<Coordinates> resolve(String googleMapsUrl) {
        if (googleMapsUrl == null || googleMapsUrl.isBlank()) {
            return Optional.empty();
        }

        String url = googleMapsUrl.trim();

        try {
            String expandedUrl = expandIfShort(url);

            Optional<Coordinates> directCoords = extractCoordinates(expandedUrl);
            if (directCoords.isPresent()) {
                return directCoords;
            }

            Optional<Coordinates> dataCoords = extractCoordinatesFromDataFragment(expandedUrl);
            if (dataCoords.isPresent()) {
                return dataCoords;
            }

            return geocodeWithNominatim(expandedUrl);
        } catch (Exception e) {
            log.warn("Failed to resolve location from URL [{}]: {}", url, e.getMessage());
            return Optional.empty();
        }
    }

    private String expandIfShort(String url) {
        if (!isShortUrl(url)) {
            return url;
        }

        if (unshortenMeToken == null || unshortenMeToken.isBlank()) {
            log.warn("Unshorten.me token is not configured. Cannot expand short URL: {}", url);
            return url;
        }

        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("https://unshorten.me/api/v2/unshorten")
                    .queryParam("url", url)
                    .build(true)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Token " + unshortenMeToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body != null && Boolean.TRUE.equals(body.get("success"))) {
                Object expanded = body.get("unshortened_url");
                if (expanded instanceof String expandedStr && !expandedStr.isBlank()) {
                    log.debug("Expanded short URL [{}] to [{}]", url, expandedStr);
                    return expandedStr;
                }
            }
        } catch (RestClientException e) {
            log.warn("Unshorten.me failed for [{}]: {}", url, e.getMessage());
        }

        return url;
    }

    private boolean isShortUrl(String url) {
        String lower = url.toLowerCase();
        return lower.contains("maps.app.goo.gl") || lower.contains("goo.gl/maps") || lower.contains("maps.google.com");
    }

    private Optional<Coordinates> extractCoordinates(String url) {
        Matcher matcher = COORDINATE_PATTERN.matcher(url);
        if (!matcher.find()) {
            return Optional.empty();
        }

        String[] parts = matcher.group(0).split(",");
        try {
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());

            if (!isValidLatitude(latitude) || !isValidLongitude(longitude)) {
                return Optional.empty();
            }

            return Optional.of(new Coordinates(
                    BigDecimal.valueOf(latitude),
                    BigDecimal.valueOf(longitude)
            ));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<Coordinates> extractCoordinatesFromDataFragment(String url) {
        Matcher latMatcher = DATA_LAT_PATTERN.matcher(url);
        Matcher lngMatcher = DATA_LNG_PATTERN.matcher(url);

        if (latMatcher.find() && lngMatcher.find()) {
            try {
                double latitude = Double.parseDouble(latMatcher.group(1));
                double longitude = Double.parseDouble(lngMatcher.group(1));

                if (isValidLatitude(latitude) && isValidLongitude(longitude)) {
                    return Optional.of(new Coordinates(
                            BigDecimal.valueOf(latitude),
                            BigDecimal.valueOf(longitude)
                    ));
                }
            } catch (NumberFormatException e) {
                // Ignore and fall through.
            }
        }

        return Optional.empty();
    }

    private Optional<Coordinates> geocodeWithNominatim(String url) {
        String query = extractSearchQuery(url);
        if (query == null || query.isBlank()) {
            return Optional.empty();
        }

        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("format", "jsonv2")
                    .queryParam("q", query)
                    .queryParam("limit", 1)
                    .build(true)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept-Language", "en-US,en");
            headers.set("User-Agent", "MyClasses Backend/1.0");
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            List<?> results = response.getBody();
            if (results == null || results.isEmpty()) {
                return Optional.empty();
            }

            Map<String, Object> result = (Map<String, Object>) results.get(0);
            double latitude = Double.parseDouble(result.get("lat").toString());
            double longitude = Double.parseDouble(result.get("lon").toString());

            return Optional.of(new Coordinates(
                    BigDecimal.valueOf(latitude),
                    BigDecimal.valueOf(longitude)
            ));
        } catch (Exception e) {
            log.warn("Nominatim geocoding failed for [{}]: {}", query, e.getMessage());
            return Optional.empty();
        }
    }

    private String extractSearchQuery(String url) {
        try {
            String decoded = java.net.URLDecoder.decode(url, StandardCharsets.UTF_8);

            String[] patterns = {
                    "/place/([^/@?&]+)",
                    "/search/([^/@?&]+)",
                    "[?&]q=([^&]+)",
                    "[?&]query=([^&]+)"
            };

            for (String pattern : patterns) {
                Matcher matcher = Pattern.compile(pattern).matcher(decoded);
                if (matcher.find()) {
                    return matcher.group(1).replace("+", " ").replace("_", " ").trim();
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract search query from URL: {}", e.getMessage());
        }

        return null;
    }

    private boolean isValidLatitude(double value) {
        return value >= -90 && value <= 90;
    }

    private boolean isValidLongitude(double value) {
        return value >= -180 && value <= 180;
    }

    public record Coordinates(BigDecimal latitude, BigDecimal longitude) {
    }
}
