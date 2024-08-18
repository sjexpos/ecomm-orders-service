package io.oigres.ecomm.service.orders.model.carts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertOrderPublicationResponseDTO implements Serializable {
    private Long publicationId;
    private Integer amount;
    private BigDecimal price;
}
