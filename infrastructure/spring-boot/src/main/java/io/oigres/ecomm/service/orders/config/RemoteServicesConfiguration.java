package io.oigres.ecomm.service.orders.config;

import io.oigres.ecomm.service.products.api.StockTransactionServiceProxy;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.users.api.UsersService;
import io.oigres.ecomm.service.users.api.UsersServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RemoteServicesConfiguration {
    @Value("${ecomm.service.products.baseUri}")
    private String productsBaseUri;
    @Value("${ecomm.service.users.baseUri}")
    private String usersBaseUri;

    @Bean
    public StockTransactionsService stockTransactionService(WebClient.Builder builder) {
        return new StockTransactionServiceProxy(builder.baseUrl(productsBaseUri).build());
    }

    @Bean
    public UsersService asyncConsumerUsersService(WebClient.Builder builder) {
        return new UsersServiceProxy(builder.baseUrl(usersBaseUri).build());
    }

}
