package io.oigres.ecomm.service.orders.usecases.orders.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.exception.OrderNotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;

@Component
public class GetOrderByIdUseCaseImpl implements GetOrderByIdUseCase {
    private final OrderRepository orderRepository;

    public GetOrderByIdUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order handle(long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }
}
