package io.oigres.ecomm.service.orders.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "ecomm.service.orders.scheduling", name="enabled", havingValue="true", matchIfMissing = true)
public class SchedulingConfiguration {
}
