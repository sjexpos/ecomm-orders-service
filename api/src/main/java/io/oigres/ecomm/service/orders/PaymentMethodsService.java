package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.cart.GetAllPaymentMethodsResponse;
import io.swagger.v3.oas.annotations.Parameter;

public interface PaymentMethodsService {
    PageResponse<GetAllPaymentMethodsResponse> getAllPaymentMethods(@Parameter(hidden = true, required = true) PageableRequest pageable);
}
