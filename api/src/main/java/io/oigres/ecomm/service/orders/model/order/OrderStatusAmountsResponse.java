package io.oigres.ecomm.service.orders.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusAmountsResponse implements Serializable {
    private List<OrderStatusAmountDto> status = new ArrayList<>();
}
