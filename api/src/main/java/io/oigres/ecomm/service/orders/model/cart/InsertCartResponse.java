package io.oigres.ecomm.service.orders.model.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertCartResponse {
    private List<InsertOrderResponse> orders;
}
