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

package io.oigres.ecomm.service.orders.repository;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends GenericRepository<Order, Long> {
  Optional<Order> findById(long id);

  Optional<Order> findByIdAndDispensaryId(long id, long dispensaryId);

  Page<Order> findAllByUserId(long userId, Pageable pageable);

  Page<Order> findAllByDispensaryId(long dispensaryId, Pageable pageable);

  Page<Order> findAllByDispensaryIdAndLastStatus(
      long dispensaryId, OrderStatusEnum lastStatus, Pageable pageable);

  Page<Order> findAllByLastStatus(OrderStatusEnum lastStatus, Pageable pageable);

  Page<Order> findAll(Pageable pageable);

  BigDecimal sumOrdersTotalsByLastStatus(OrderStatusEnum status);

  BigDecimal sumOrdersTotals();

  Order save(Order order);

  Long countByLastStatusAndDispensaryId(OrderStatusEnum status, Long dispensaryId);

  Long countByLastStatus(OrderStatusEnum status);
}
