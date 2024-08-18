package io.oigres.ecomm.service.orders.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import io.oigres.ecomm.service.orders.OrderStatusEnumApi;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusAmountDto implements Serializable {
    private OrderStatusEnumApi name;
    private Long amount;
}
