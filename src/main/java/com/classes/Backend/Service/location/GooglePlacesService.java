package com.classes.Backend.Service.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class GooglePlacesService {

    private static final String TEXT_SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    private static final String FIELD_MASK = "places.id,places.displayName,places.rating,places.userRatingCount,places.location";
    private static final double LOCATION_BIAS_RADIUS_METERS = 500.0;

    private final RestTemplate restTemplate;
    private final LocationResolverService locationResolverService;
    private final String apiKey;

    public GooglePlacesService(
            LocationResolverService locationResolverService,
            @Value("${google.places.api-key:}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.locationResolverService = locationResolverService;
        this.apiKey = apiKey;
    }

    public Optional<GooglePlaceInfo> fetchPlaceInfo(String googleMapsUrl, BigDecimal latitude,
                                                    BigDecimal longitude, String fallbackName) {
        if (!StringUtils.hasText(apiKey)) {
            log.warn("Google Places API key is not configured. Skipping rating fetch.");
            return Optional.empty();
        }
        if (!StringUtils.hasText(googleMapsUrl)) {
            return Optional.empty();
        }

        try {
            String expandedUrl = locationResolverService.expandUrl(googleMapsUrl.trim());
            String query = locationResolverService.extractPlaceQuery(expandedUrl);
            if (!StringUtils.hasText(query)) {
                query = fallbackName;
            }
            if (!StringUtils.hasText(query)) {
                return Optional.empty();
            }

            Map<String, Object> body = new HashMap<>();
            body.put("textQuery", query);
            body.put("pageSize", 1);
            if (latitude != null && longitude != null) {
                body.put("locationBias", Map.of("circle", Map.of(
                        "center", Map.of(
                                "latitude", latitude.doubleValue(),
                                "longitude", longitude.doubleValue()
                        ),
                        "radius", LOCATION_BIAS_RADIUS_METERS
                )));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", apiKey);
            headers.set("X-Goog-FieldMask", FIELD_MASK);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    TEXT_SEARCH_URL, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                return Optional.empty();
            }
            Object places = responseBody.get("places");
            if (!(places instanceof List<?> placeList) || placeList.isEmpty()) {
                log.warn("No Google place found for query '{}'", query);
                return Optional.empty();
            }

            Object first = placeList.get(0);
            if (!(first instanceof Map<?, ?> place)) {
                return Optional.empty();
            }

            String placeId = place.get("id") instanceof String id ? id : null;
            BigDecimal rating = toBigDecimal(place.get("rating"));
            Integer ratingCount = toInteger(place.get("userRatingCount"));

            BigDecimal placeLat = null;
            BigDecimal placeLng = null;
            if (place.get("location") instanceof Map<?, ?> location) {
                placeLat = toBigDecimal(location.get("latitude"));
                placeLng = toBigDecimal(location.get("longitude"));
            }

            return Optional.of(new GooglePlaceInfo(placeId, rating, ratingCount, placeLat, placeLng));
        } catch (Exception e) {
            log.warn("Google Places lookup failed for URL [{}]: {}", googleMapsUrl, e.getMessage());
            return Optional.empty();
        }
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return null;
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    public record GooglePlaceInfo(String placeId, BigDecimal rating, Integer ratingCount,
                                  BigDecimal latitude, BigDecimal longitude) {
    }
}
