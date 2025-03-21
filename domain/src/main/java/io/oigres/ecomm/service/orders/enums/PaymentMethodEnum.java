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

package io.oigres.ecomm.service.orders.enums;

import java.util.Optional;

public enum PaymentMethodEnum {
  CASH(1, "Cash");

  private final Integer id;
  private final String prettyName;

  PaymentMethodEnum(Integer id, String prettyName) {
    this.id = id;
    this.prettyName = prettyName;
  }

  public Integer getId() {
    return id;
  }

  public String getPrettyName() {
    return prettyName;
  }

  public static Optional<PaymentMethodEnum> getByName(String name) {
    for (PaymentMethodEnum e : values()) {
      if (e.getPrettyName().equalsIgnoreCase(name) || e.name().equalsIgnoreCase(name)) {
        return Optional.of(e);
      }
    }
    return Optional.empty();
  }
}
