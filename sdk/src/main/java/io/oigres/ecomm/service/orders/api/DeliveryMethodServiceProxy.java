package io.oigres.ecomm.service.orders.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import io.oigres.ecomm.service.orders.DeliveryMethodsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.JacksonPageImpl;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.cart.GetAllDeliveryMethodsResponse;

import java.time.Duration;

public class DeliveryMethodServiceProxy extends MiddlewareProxy implements DeliveryMethodsService {

    public DeliveryMethodServiceProxy(WebClient webClient) {
        super(webClient);
    }

    public DeliveryMethodServiceProxy(final String baseUri) {
        super(baseUri, Duration.ofMillis(2000));
    }

    @Override
    public PageResponse<GetAllDeliveryMethodsResponse> getAllDeliveryMethods(PageableRequest pageable) {
        ParameterizedTypeReference<JacksonPageImpl<GetAllDeliveryMethodsResponse>> typeReference = new ParameterizedTypeReference<>() {};
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path(Routes.CARTS_CONTROLLER_PATH.concat(Routes.GET_ALL_DELIVERY_METHODS))
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build(pageable)
                )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeReference).block();
    }
}
