package io.oigres.ecomm.service.orders.config;

import io.oigres.ecomm.service.orders.rpc.StockTransactionsServiceProxyEnhanced;
import io.oigres.ecomm.service.orders.rpc.UsersServiceProxyEnhanced;
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

    @Bean
    public StockTransactionsService stockTransactionService(
            WebClient.Builder builder,
            @Value("${ecomm.service.products.baseUri}") String productsBaseUri
    ) {
        return new StockTransactionsServiceProxyEnhanced(new StockTransactionServiceProxy(builder.baseUrl(productsBaseUri).build()));
    }

    @Bean
    public UsersService asyncConsumerUsersService(
            WebClient.Builder builder,
            @Value("${ecomm.service.users.baseUri}") String usersBaseUri
    ) {
        return new UsersServiceProxyEnhanced(new UsersServiceProxy(builder.baseUrl(usersBaseUri).build()));
    }

}
