/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.orders.usecases.orders.changeStatus;

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
import org.springframework.stereotype.Component;

@Component
public class ChangeOrderStatusUseCaseImpl implements ChangeOrderStatusUseCase {
  private final OrderRepository orderRepository;
  private final StatusRepository statusRepository;
  private final OrderStatusRepository orderStatusRepository;

  public ChangeOrderStatusUseCaseImpl(
      OrderRepository orderRepository,
      StatusRepository statusRepository,
      OrderStatusRepository orderStatusRepository) {
    this.orderRepository = orderRepository;
    this.statusRepository = statusRepository;
    this.orderStatusRepository = orderStatusRepository;
  }

  @Override
  public Order handle(long orderId, long dispensaryId) throws NotFoundException {
    Order order =
        orderRepository
            .findByIdAndDispensaryId(orderId, dispensaryId)
            .orElseThrow(OrderNotFoundException::new);
    OrderStatusEnum currentStatus = order.getLastStatus();
    OrderStatusEnum nextStatus = OrderStatusEnum.getNextStatus(currentStatus);
    order.setLastStatus(nextStatus);
    Status newStatus =
        statusRepository
            .findByName(nextStatus)
            .orElseThrow(() -> new NotFoundException("Status not found"));
    OrderStatus newOrderStatus =
        OrderStatus.builder().status(newStatus).order(order).date(LocalDateTime.now()).build();
    orderStatusRepository.save(newOrderStatus);
    orderRepository.save(order);
    return order;
  }
}
