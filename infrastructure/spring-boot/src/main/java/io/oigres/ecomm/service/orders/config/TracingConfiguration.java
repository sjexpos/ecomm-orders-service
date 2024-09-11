package io.oigres.ecomm.service.orders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Configuration
public class TracingConfiguration {

    @Data
    @ConfigurationProperties(prefix = "ecomm.service.tracing") // it's not working
    public static class TracingProperties {
        @NotNull
        @NotBlank
        private String url;
    }

    @Bean
    public OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${ecomm.service.tracing.url}") String url) {
        return OtlpHttpSpanExporter.builder()
            .setEndpoint(url)
            .build();
    }

}
