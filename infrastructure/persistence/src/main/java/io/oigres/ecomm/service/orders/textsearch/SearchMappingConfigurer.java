package io.oigres.ecomm.service.orders.textsearch;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.domain.Order;

@Component("searchMappingConfigurer")
public class SearchMappingConfigurer implements HibernateOrmSearchMappingConfigurer {

    @Override
    public void configure(HibernateOrmMappingConfigurationContext context) {
        ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
        configureOrderIndexes(mapping);
    }

    private void configureOrderIndexes(ProgrammaticMappingConfigurationContext mapping) {
        TypeMappingStep orderMapping = mapping.type(Order.class);
        orderMapping.indexed();
        orderMapping.property("id")
                .documentId();
    }

}
