package io.oigres.ecomm.service.orders.model.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertPublicationWithAmountRequest {
    @Schema(name = "publicationId", required = true)
    @NotNull(message = "publicationId must not be null")
    private Long publicationId;
    @Schema(name = "amount", required = true)
    @NotNull(message = "amount must not be null")
    @Positive(message = "amount must be greater than 0")
    private Integer amount;
}
