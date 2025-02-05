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

package io.oigres.ecomm.service.orders.usecases.orders.total;

import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.NotFoundException;
import io.oigres.ecomm.service.orders.repository.OrderRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class GetOrdersCountUseCaseImpl implements GetOrdersCountUseCase {
  private final OrderRepository orderRepository;

  public GetOrdersCountUseCaseImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public BigDecimal getOrdersCount(String status) throws NotFoundException {
    if (status != null) {
      OrderStatusEnum statusEnum =
          OrderStatusEnum.getByName(status)
              .orElseThrow(
                  () -> new NotFoundException(String.format("Status %s not found", status)));
      return orderRepository.sumOrdersTotalsByLastStatus(statusEnum);
    } else {
      return orderRepository.sumOrdersTotals();
    }
  }
}
