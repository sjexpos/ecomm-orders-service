package io.oigres.ecomm.service.orders.api;

import org.springframework.web.reactive.function.client.WebClient;

import io.oigres.ecomm.service.orders.CartsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.cart.InsertCartRequest;
import io.oigres.ecomm.service.orders.model.cart.InsertCartResponse;

import java.time.Duration;
import java.util.function.Supplier;

public class CartsServiceProxy extends MiddlewareProxy implements CartsService {
    public CartsServiceProxy(WebClient webClient, Supplier<String> traceIdExtractor) {
        super(webClient, traceIdExtractor);
    }

    public CartsServiceProxy(final String baseUri, final Supplier<String> traceIdExtractor) {
        super(baseUri, Duration.ofMillis(2000), traceIdExtractor);
    }

    @Override
    public InsertCartResponse createCart(InsertCartRequest request) {
        return post(uriBuilder -> uriBuilder
                .path(Routes.CARTS_CONTROLLER_PATH)
                .build(), request, InsertCartResponse.class);
    }
}
