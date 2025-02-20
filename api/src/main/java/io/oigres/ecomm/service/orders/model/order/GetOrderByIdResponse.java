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

package io.oigres.ecomm.service.orders.model.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetOrderByIdResponse implements Serializable {
  private Long id;
  private LocalDateTime orderDate;
  @Singular private List<OrderPublicationResponse> items;
  @Singular private List<GetStatusesResponse> statuses;
  private Long dispensaryId;
  private Long userId;
  private String deliveryMethod;
  private String paymentMethod;
  private BigDecimal subtotal;
  private BigDecimal exciseTax;
  private BigDecimal salesTax;
  private BigDecimal total;

  public List<OrderPublicationResponse> getItems() {
    return List.copyOf(items);
  }

  public List<GetStatusesResponse> getStatuses() {
    return List.copyOf(statuses);
  }
}
