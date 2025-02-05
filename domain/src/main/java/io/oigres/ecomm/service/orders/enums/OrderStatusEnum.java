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

import io.oigres.ecomm.service.orders.exception.NotFoundException;
import java.util.Optional;

public enum OrderStatusEnum {
  ORDERED(1L, "Ordered", 2L),
  CONFIRMED(2L, "Confirmed", 3L),
  READY(3L, "Ready", 4L),
  DELIVERED(4L, "Delivered", null),
  CANCELED(5L, "Canceled", null);
  private final Long id;
  private final String prettyName;
  private final Long nextStatusId;

  OrderStatusEnum(Long id, String prettyName, Long nextStatusId) {
    this.id = id;
    this.prettyName = prettyName;
    this.nextStatusId = nextStatusId;
  }

  public Long getId() {
    return id;
  }

  public String getPrettyName() {
    return prettyName;
  }

  public static OrderStatusEnum getNextStatus(OrderStatusEnum statusEnum) throws NotFoundException {
    if (statusEnum.getNextStatusId() == null) {
      throw new NotFoundException(
          String.format("There is no next status for current status %s", statusEnum.name()));
    } else {
      return getById(statusEnum.getNextStatusId()).get();
    }
  }

  public static Optional<OrderStatusEnum> getByName(String name) {
    for (OrderStatusEnum e : values()) {
      if (e.getPrettyName().equalsIgnoreCase(name) || e.name().equalsIgnoreCase(name)) {
        return Optional.of(e);
      }
    }
    return Optional.empty();
  }

  public static Optional<OrderStatusEnum> getById(Long id) {
    for (OrderStatusEnum e : values()) {
      if (e.getId().equals(id)) {
        return Optional.of(e);
      }
    }
    return Optional.empty();
  }

  public Long getNextStatusId() {
    return nextStatusId;
  }
}
