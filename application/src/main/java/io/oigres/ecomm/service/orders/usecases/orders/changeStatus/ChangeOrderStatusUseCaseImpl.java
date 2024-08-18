package io.oigres.ecomm.service.orders.usecases.orders.changeStatus;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.domain.OrderStatus;
import io.oigres.ecomm.service.orders.domain.Status;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;
import io.oigres.ecomm.service.orders.exception.OrderNotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;
import io.oigres.ecomm.service.orders.repository.OrderStatusRepository;
import io.oigres.ecomm.service.orders.repository.StatusRepository;

import java.time.LocalDateTime;

@Component
public class ChangeOrderStatusUseCaseImpl implements ChangeOrderStatusUseCase {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final OrderStatusRepository orderStatusRepository;

    public ChangeOrderStatusUseCaseImpl(OrderRepository orderRepository, StatusRepository statusRepository, OrderStatusRepository orderStatusRepository) {
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public Order handle(long orderId, long dispensaryId) throws NotFoundException {
        Order order = orderRepository.findByIdAndDispensaryId(orderId, dispensaryId).orElseThrow(OrderNotFoundException::new);
        OrderStatusEnum currentStatus = order.getLastStatus();
        OrderStatusEnum nextStatus = OrderStatusEnum.getNextStatus(currentStatus);
        order.setLastStatus(nextStatus);
        Status newStatus = statusRepository.findByName(nextStatus).orElseThrow(() -> new NotFoundException("Status not found"));
        OrderStatus newOrderStatus = OrderStatus.builder().status(newStatus).order(order).date(LocalDateTime.now()).build();
        orderStatusRepository.save(newOrderStatus);
        orderRepository.save(order);
        return order;
    }
}
