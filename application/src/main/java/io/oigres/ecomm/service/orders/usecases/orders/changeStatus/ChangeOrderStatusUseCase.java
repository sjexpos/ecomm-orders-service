package io.oigres.ecomm.service.orders.usecases.orders.changeStatus;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.exception.NotFoundException;

public interface ChangeOrderStatusUseCase {
    Order handle(long orderId, long dispensaryId) throws NotFoundException;
}
