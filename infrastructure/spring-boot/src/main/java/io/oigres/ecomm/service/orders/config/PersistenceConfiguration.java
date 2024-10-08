package io.oigres.ecomm.service.orders.config;

import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.oigres.ecomm.service.orders.domain.Auditable;
import io.oigres.ecomm.service.orders.repository.SearchRepositoryImpl;

@Configuration
@EnableJpaRepositories( basePackages="io.oigres.ecomm.service.orders.repository", repositoryBaseClass = SearchRepositoryImpl.class )
@EntityScan( "io.oigres.ecomm.service.orders.domain" )
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfiguration {

    static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.empty();
        }
    }

	@Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
            EntityManagerFactoryBuilder builder, ConfigurableListableBeanFactory beanFactory) {

        return builder.dataSource(dataSource)
                .packages(Auditable.class)
                .properties(Map.of(org.hibernate.cfg.AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory)))
                .build();
    }

}
