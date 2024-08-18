package io.oigres.ecomm.service.orders.model.carts;

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
public class InsertCartResponseDTO {
    private List<InsertOrderResponseDTO> orders;
}
