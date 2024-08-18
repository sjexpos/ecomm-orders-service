package io.oigres.ecomm.service.orders.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeCurrentOrderStatusResponse implements Serializable {
    private String currentStatus;
    private List<GetStatusesResponse> statuses;
}
