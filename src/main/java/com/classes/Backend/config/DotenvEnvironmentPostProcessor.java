package com.classes.Backend.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads a local {@code .env} file into the Spring Environment so that variables
 * defined there are available for placeholder resolution in
 * {@code application.properties}.
 *
 * <p>This is intended for local development only. In production (e.g. DigitalOcean
 * App Platform), environment variables are injected directly by the platform and
 * this post-processor safely does nothing.</p>
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DOTENV = ".env";
    private static final String PROPERTY_SOURCE_NAME = "dotenv";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = resolveDotenvResource();
        if (resource == null || !resource.exists() || !resource.isReadable()) {
            return;
        }

        Map<String, Object> properties = load(resource);
        if (!properties.isEmpty()) {
            environment.getPropertySources()
                    .addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
        }
    }

    private Resource resolveDotenvResource() {
        try {
            // Looks for .env on the filesystem relative to the working directory.
            return new PathMatchingResourcePatternResolver()
                    .getResource("file:./" + DOTENV);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> load(Resource resource) {
        Map<String, Object> properties = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines().forEach(line -> {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    return;
                }
                int separatorIndex = trimmed.indexOf('=');
                if (separatorIndex <= 0) {
                    return;
                }
                String key = trimmed.substring(0, separatorIndex).trim();
                String value = trimmed.substring(separatorIndex + 1).trim();
                // Remove surrounding quotes if present.
                value = unquote(value);
                if (!key.isEmpty()) {
                    properties.put(key, value);
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + DOTENV + " file", e);
        }
        return properties;
    }

    private String unquote(String value) {
        if (value.length() >= 2
                && ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'")))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
