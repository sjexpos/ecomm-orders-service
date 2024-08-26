package io.oigres.ecomm.service.orders.api;

import org.springframework.web.reactive.function.client.WebClient;

import io.oigres.ecomm.service.orders.CartsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.cart.InsertCartRequest;
import io.oigres.ecomm.service.orders.model.cart.InsertCartResponse;

import java.time.Duration;

public class CartsServiceProxy extends MiddlewareProxy implements CartsService {

    public CartsServiceProxy(WebClient webClient) {
        super(webClient);
    }

    public CartsServiceProxy(final String baseUri) {
        super(baseUri, Duration.ofMillis(2000));
    }

    @Override
    public InsertCartResponse createCart(InsertCartRequest request) {
        return post(uriBuilder -> uriBuilder
                .path(Routes.CARTS_CONTROLLER_PATH)
                .build(), request, InsertCartResponse.class);
    }
}
