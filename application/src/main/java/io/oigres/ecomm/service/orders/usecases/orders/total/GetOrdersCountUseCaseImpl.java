package io.oigres.ecomm.service.orders.usecases.orders.total;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;

import java.math.BigDecimal;

@Component
public class GetOrdersCountUseCaseImpl implements GetOrdersCountUseCase {
    private final OrderRepository orderRepository;

    public GetOrdersCountUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public BigDecimal getOrdersCount(String status) throws NotFoundException {
        if (status != null) {
            OrderStatusEnum statusEnum = OrderStatusEnum.getByName(status).orElseThrow(() -> new NotFoundException(String.format("Status %s not found", status)));
            return orderRepository.sumOrdersTotalsByLastStatus(statusEnum);
        } else {
            return orderRepository.sumOrdersTotals();
        }
    }
}
