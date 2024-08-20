package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.cart.GetAllDeliveryMethodsResponse;
import io.swagger.v3.oas.annotations.Parameter;

public interface DeliveryMethodsService {
    PageResponse<GetAllDeliveryMethodsResponse> getAllDeliveryMethods(@Parameter(hidden = true, required = true) PageableRequest pageable);
}
