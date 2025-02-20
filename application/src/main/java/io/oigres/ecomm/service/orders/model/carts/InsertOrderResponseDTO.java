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

package io.oigres.ecomm.service.orders.model.carts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsertOrderResponseDTO implements Serializable {
  private Long orderId;
  private Long dispensaryId;
  @Singular private List<InsertOrderPublicationResponseDTO> publications;
  private String deliveryMethod;
  private String paymentMethod;
  private String status;
  private BigDecimal subtotal;
  private BigDecimal exciseTax;
  private BigDecimal salesTax;
  private BigDecimal total;

  public List<InsertOrderPublicationResponseDTO> getPublications() {
    return List.copyOf(publications);
  }
}
