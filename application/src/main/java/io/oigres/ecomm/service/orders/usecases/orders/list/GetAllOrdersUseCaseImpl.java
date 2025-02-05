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

package io.oigres.ecomm.service.orders.usecases.orders.list;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class GetAllOrdersUseCaseImpl implements GetAllOrdersUseCase {
  private final OrderRepository orderRepository;

  public GetAllOrdersUseCaseImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Page<Order> handle(
      Long dispensaryId, Long userId, OrderStatusEnum status, Pageable pageable)
      throws NotFoundException {
    if (userId != null) {
      return orderRepository.findAllByUserId(userId, pageable);
    }
    if (dispensaryId != null) {
      if (status != null) {
        OrderStatusEnum statusEnum =
            OrderStatusEnum.getByName(status.name())
                .orElseThrow(
                    () -> new NotFoundException(String.format("Status %s not found", status)));
        return orderRepository.findAllByDispensaryIdAndLastStatus(
            dispensaryId, statusEnum, pageable);
      } else {
        return orderRepository.findAllByDispensaryId(dispensaryId, pageable);
      }
    }
    if (status != null) {
      OrderStatusEnum statusEnum =
          OrderStatusEnum.getByName(status.name())
              .orElseThrow(
                  () -> new NotFoundException(String.format("Status %s not found", status)));
      return orderRepository.findAllByLastStatus(statusEnum, pageable);
    }
    return orderRepository.findAll(pageable);
  }
}
