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

package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.model.NotFoundException;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.order.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface OrdersService {
  GetOrderByIdResponse getOrderById(
      @Parameter(
              name = "orderId",
              required = true,
              description = "identifier associated with the order (0..N)")
          @Min(value = 1, message = "orderId should be greater than zero")
          long orderId)
      throws NotFoundException;

  PageResponse<GetAllOrdersResponse> getAllOrders(
      Long dispensaryId,
      Long userId,
      OrderStatusEnumApi status,
      @Parameter(hidden = true, required = true) PageableRequest pageable);

  OrdersCountResponse getOrdersCount(Long dispensaryId, Long userId, OrderStatusEnumApi status);

  ChangeCurrentOrderStatusResponse changeOrderStatus(
      @Parameter(
              name = "orderId",
              required = true,
              description = "identifier associated with the order (0..N)")
          @Min(value = 1, message = "orderId should be greater than zero")
          long orderId,
      @Valid ChangeCurrentOrderStatusRequest request)
      throws NotFoundException;

  OrderStatusAmountsResponse getOrdersStatusAmount(
      @Parameter(
              name = "dispensaryId",
              required = false,
              description = "identifier associated with the dispensary logged in (1..N)")
          @Min(value = 1, message = "dispensaryId should be greater than zero")
          Long dispensaryId)
      throws NotFoundException;
}
