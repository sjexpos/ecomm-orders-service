package io.oigres.ecomm.service.orders.model.carts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertPublicationWithAmountRequestDTO {
    private Long publicationId;
    private Integer amount;
}
