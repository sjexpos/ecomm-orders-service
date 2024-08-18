package io.oigres.ecomm.service.orders.model.carts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertCartRequestDTO {
    private Long userId;
    private List<InsertPublicationWithAmountRequestDTO> publications;
}
