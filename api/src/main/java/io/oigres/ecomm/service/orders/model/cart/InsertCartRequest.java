package io.oigres.ecomm.service.orders.model.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertCartRequest {
    @Schema(name = "userId", required = true)
    @NotNull(message = "userId must not be null")
    private Long userId;
    @Schema(name = "publications", required = true)
    @NotNull(message = "publications must not be null")
    @Size(min = 1, message = "publications must have at least 1 element")
    private List<InsertPublicationWithAmountRequest> publications;
}
