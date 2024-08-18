package io.oigres.ecomm.service.orders.usecases.orders.search;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.exception.OrderNotFoundException;

public interface GetOrderByIdUseCase {
    Order handle(long orderId) throws OrderNotFoundException;
}
