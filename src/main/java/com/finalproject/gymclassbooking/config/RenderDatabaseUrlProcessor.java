package com.finalproject.gymclassbooking.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RenderDatabaseUrlProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }
        if (!databaseUrl.startsWith("postgres://") && !databaseUrl.startsWith("postgresql://")) {
            return;
        }

        URI uri = URI.create(databaseUrl);
        String[] credentials = parseCredentials(uri.getUserInfo());
        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
        if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
            jdbcUrl += "?" + uri.getQuery();
        }

        Map<String, Object> datasourceProperties = new HashMap<>();
        datasourceProperties.put("spring.datasource.url", jdbcUrl);
        if (credentials[0] != null) {
            datasourceProperties.put("spring.datasource.username", credentials[0]);
        }
        if (credentials[1] != null) {
            datasourceProperties.put("spring.datasource.password", credentials[1]);
        }

        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("renderDatabaseUrl", datasourceProperties));
    }

    private String[] parseCredentials(String userInfo) {
        if (userInfo == null || userInfo.isBlank()) {
            return new String[]{null, null};
        }
        String[] parts = userInfo.split(":", 2);
        String username = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
        String password = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : null;
        return new String[]{username, password};
    }
}
