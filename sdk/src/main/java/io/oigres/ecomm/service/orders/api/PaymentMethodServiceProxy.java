package io.oigres.ecomm.service.orders.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import io.oigres.ecomm.service.orders.Constants;
import io.oigres.ecomm.service.orders.PaymentMethodsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.JacksonPageImpl;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequestImpl;
import io.oigres.ecomm.service.orders.model.cart.GetAllPaymentMethodsResponse;

import java.time.Duration;
import java.util.function.Supplier;

public class PaymentMethodServiceProxy extends MiddlewareProxy implements PaymentMethodsService {
    public PaymentMethodServiceProxy(WebClient webClient, Supplier<String> traceIdExtractor) {
        super(webClient, traceIdExtractor);
    }

    public PaymentMethodServiceProxy(final String baseUri, final Supplier<String> traceIdExtractor) {
        super(baseUri, Duration.ofMillis(2000), traceIdExtractor);
    }

    @Override
    public PageResponse<GetAllPaymentMethodsResponse> getAllPaymentMethods(PageableRequestImpl pageable) {
        ParameterizedTypeReference<JacksonPageImpl<GetAllPaymentMethodsResponse>> typeReference = new ParameterizedTypeReference<>() {};
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path(Routes.CARTS_CONTROLLER_PATH.concat(Routes.GET_ALL_PAYMENT_METHODS))
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build(pageable)
                )
                .header(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID, getTraceIdExtractor().get())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeReference).block();
    }
}
