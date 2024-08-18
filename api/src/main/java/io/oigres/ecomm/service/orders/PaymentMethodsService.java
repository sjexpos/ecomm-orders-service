package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequestImpl;
import io.oigres.ecomm.service.orders.model.cart.GetAllPaymentMethodsResponse;
import io.swagger.v3.oas.annotations.Parameter;

public interface PaymentMethodsService {
    PageResponse<GetAllPaymentMethodsResponse> getAllPaymentMethods(@Parameter(hidden = true, required = true) PageableRequestImpl pageable);
}
