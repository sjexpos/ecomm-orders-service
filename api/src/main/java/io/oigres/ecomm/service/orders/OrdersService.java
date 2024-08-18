package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.model.NotFoundException;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequestImpl;
import io.oigres.ecomm.service.orders.model.order.*;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.Valid;
import javax.validation.constraints.Min;

public interface OrdersService {
    GetOrderByIdResponse getOrderById(
            @Parameter(name = "orderId", required = true, description = "identifier associated with the order (0..N)") @Min(value = 1, message = "orderId should be greater than zero") long orderId)
            throws NotFoundException;

    PageResponse<GetAllOrdersResponse> getAllOrders(Long dispensaryId, Long userId, OrderStatusEnumApi status, @Parameter(hidden = true, required = true) PageableRequestImpl pageable);

    OrdersCountResponse getOrdersCount(String status);

    ChangeCurrentOrderStatusResponse changeOrderStatus(
            @Parameter(name = "orderId", required = true, description = "identifier associated with the order (0..N)") @Min(value = 1, message = "orderId should be greater than zero") long orderId,
            @Valid ChangeCurrentOrderStatusRequest request) throws NotFoundException;

    OrderStatusAmountsResponse getOrdersStatusAmount(
            @Parameter(name = "dispensaryId", required = false, description = "identifier associated with the dispensary logged in (1..N)")
            @Min(value = 1, message = "dispensaryId should be greater than zero")
            Long dispensaryId) throws NotFoundException;
}
