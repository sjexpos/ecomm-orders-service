package io.oigres.ecomm.service.orders.usecases.orders.search;

import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;

public interface GetOrderStatusAmountsUseCase {

    Long getOrderStatusAmount(OrderStatusEnum status, Long dispensaryId) throws NotFoundException;
}
