package com.finalproject.gymclassbooking.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RenderDatabaseUrlProcessorTest {

    @Test
    void convertsDbUrlIntoJdbcDatasourceProperties() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource(
                "test",
                Map.of("DB_URL", "postgresql://gym_user:secret%21@db.example.com:5433/gym_db?sslmode=require")
        ));

        new RenderDatabaseUrlProcessor().postProcessEnvironment(environment, null);

        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://db.example.com:5433/gym_db?sslmode=require");
        assertThat(environment.getProperty("spring.datasource.username")).isEqualTo("gym_user");
        assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("secret!");
    }

    @Test
    void supportsLegacyDatabaseUrlDuringDeploymentTransition() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource(
                "test",
                Map.of("DATABASE_URL", "postgres://legacy:password@localhost/gym_db")
        ));

        new RenderDatabaseUrlProcessor().postProcessEnvironment(environment, null);

        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://localhost:5432/gym_db");
    }

    @Test
    void supportsPreviousRenderHostVariablesDuringBlueprintTransition() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource(
                "test",
                Map.of(
                        "DB_HOST", "legacy-db.internal",
                        "DB_PORT", "5434",
                        "DB_NAME", "legacy_gym"
                )
        ));

        new RenderDatabaseUrlProcessor().postProcessEnvironment(environment, null);

        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://legacy-db.internal:5434/legacy_gym");
    }
}
