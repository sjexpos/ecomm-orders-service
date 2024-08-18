package io.oigres.ecomm.service.orders.usecases.orders.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;

@Component
public class GetOrderStatusAmountsUseCaseImpl implements GetOrderStatusAmountsUseCase {
    private final OrderRepository orderRepository;

    public GetOrderStatusAmountsUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long getOrderStatusAmount(OrderStatusEnum status, Long dispensaryId) throws NotFoundException {
        if (dispensaryId != null)
            return orderRepository.countByLastStatusAndDispensaryId(status,dispensaryId);
        else
            return orderRepository.countByLastStatus(status);
    }
}
