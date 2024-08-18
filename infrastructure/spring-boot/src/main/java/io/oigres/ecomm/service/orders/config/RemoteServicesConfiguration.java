package io.oigres.ecomm.service.orders.config;

import io.oigres.ecomm.service.products.Constants;
import io.oigres.ecomm.service.products.api.StockTransactionServiceProxy;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.users.api.UsersService;
import io.oigres.ecomm.service.users.api.UsersServiceProxy;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class RemoteServicesConfiguration {
    @Value("${ecomm.service.products.baseUri}")
    private String productsBaseUri;
    @Value("${ecomm.service.users.baseUri}")
    private String usersBaseUri;

    @Bean
    public StockTransactionsService stockTransactionService() {
        return new StockTransactionServiceProxy(productsBaseUri, traceIdExtractor());
    }

    @Bean
    public UsersService asyncConsumerUsersService() {
        return new UsersServiceProxy(usersBaseUri, traceIdExtractor());
    }

    private Supplier<String> traceIdExtractor() {
        return () -> MDC.get(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID);
    }
}
