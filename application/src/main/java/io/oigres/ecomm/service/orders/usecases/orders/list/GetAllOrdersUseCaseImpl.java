package io.oigres.ecomm.service.orders.usecases.orders.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;

@Component
public class GetAllOrdersUseCaseImpl implements GetAllOrdersUseCase {
    private final OrderRepository orderRepository;

    public GetAllOrdersUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> handle(Long dispensaryId, Long userId, OrderStatusEnum status, Pageable pageable) throws NotFoundException {
        if (userId != null) {
            return orderRepository.findAllByUserId(userId, pageable);
        }
        if (dispensaryId != null) {
            if (status != null) {
                OrderStatusEnum statusEnum = OrderStatusEnum.getByName(status.name()).orElseThrow(() -> new NotFoundException(String.format("Status %s not found", status)));
                return orderRepository.findAllByDispensaryIdAndLastStatus(dispensaryId, statusEnum, pageable);
            } else {
                return orderRepository.findAllByDispensaryId(dispensaryId, pageable);
            }
        }
        if (status != null) {
            OrderStatusEnum statusEnum = OrderStatusEnum.getByName(status.name()).orElseThrow(() -> new NotFoundException(String.format("Status %s not found", status)));
            return orderRepository.findAllByLastStatus(statusEnum, pageable);
        }
        return orderRepository.findAll(pageable);
    }
}
