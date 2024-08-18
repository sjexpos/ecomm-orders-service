package io.oigres.ecomm.service.orders.usecases.orders.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;

public interface GetAllOrdersUseCase {
    Page<Order> handle(Long dispensaryId, Long userId, OrderStatusEnum status, Pageable pageable) throws NotFoundException;
}
