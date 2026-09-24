package com.shop.ordering.infrastruture.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing(
        dateTimeProviderRef = "provedorDataHoraAuditoria",
        auditorAwareRef = "provedorAuditoria"
)
public class ConfigAuditoriaDadosSpring {

    @Bean
    public DateTimeProvider provedorDataHoraAuditoria() {
        return () -> Optional.of(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }

    @Bean
    public AuditorAware<UUID> provedorAuditoria() {
        return () -> Optional.of(UUID.randomUUID());
    }
}
