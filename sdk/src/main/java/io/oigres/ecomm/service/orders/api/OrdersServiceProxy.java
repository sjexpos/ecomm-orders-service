package io.oigres.ecomm.service.orders.api;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import io.oigres.ecomm.service.orders.OrderStatusEnumApi;
import io.oigres.ecomm.service.orders.OrdersService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.order.*;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

public class OrdersServiceProxy extends MiddlewareProxy implements OrdersService, AsyncOrdersService {

    public OrdersServiceProxy(WebClient webClient) {
        super(webClient);
    }

    public OrdersServiceProxy(final String baseUri) {
        super(baseUri, Duration.ofMillis(2000));
    }

    // --------------------------------- getOrderById --------------------------------- //

    private Function<UriBuilder, URI> getOrderById_Call(long orderId) {
        return uriBuilder -> uriBuilder
                .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.GET_ORDER_BY_ID))
                .build(orderId);
    }

    @Override
    public GetOrderByIdResponse getOrderById(long orderId) {
        return get(getOrderById_Call(orderId), GetOrderByIdResponse.class);
    }

    // --------------------------------- getAllOrders --------------------------------- //

    @Override
    public PageResponse<GetAllOrdersResponse> getAllOrders(Long dispensaryId, Long userId, OrderStatusEnumApi status, PageableRequest pageable) {
        return getPage(uriBuilder -> uriBuilder
                        .path(Routes.ORDERS_CONTROLLER_PATH)
                        .queryParamIfPresent("dispensaryId", Optional.ofNullable(dispensaryId))
                        .queryParamIfPresent("userId", Optional.ofNullable(userId))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build(dispensaryId, userId, status, pageable),
                GetAllOrdersResponse.class
        );
    }

    // --------------------------------- getOrdersCount --------------------------------- //

    private Function<UriBuilder, URI> getOrdersCount_Call(String status) {
        return uriBuilder -> uriBuilder
                .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.GET_TOTAL_ORDERS))
                .build(status);
    }

    @Override
    public OrdersCountResponse getOrdersCount(String status) {
        return get(getOrdersCount_Call(status), OrdersCountResponse.class);
    }

    @Override
    public Future<OrdersCountResponse> getOrdersCountAsync(String status) {
        return getAsync(getOrdersCount_Call(status), OrdersCountResponse.class);
    }

    // --------------------------------- changeOrderStatus --------------------------------- //

    @Override
    public ChangeCurrentOrderStatusResponse changeOrderStatus(long orderId, ChangeCurrentOrderStatusRequest request) {
        return patch(
                uriBuilder -> uriBuilder
                        .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.CHANGE_ORDER_STATUS))
                        .build(orderId),
                request,
                ChangeCurrentOrderStatusResponse.class
        );
    }

    // --------------------------------- getOrdersStatusAmount --------------------------------- //

    private Function<UriBuilder, URI> getOrdersStatusAmount_Call(Long dispensaryId) {
        return uriBuilder -> uriBuilder
                .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.GET_ORDER_STATUS_AMOUNTS))
                .build(dispensaryId);
    }

    @Override
    public OrderStatusAmountsResponse getOrdersStatusAmount(Long dispensaryId) {
        return get(getOrdersStatusAmount_Call(dispensaryId), OrderStatusAmountsResponse.class);
    }
}
