package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequestImpl;
import io.oigres.ecomm.service.orders.model.cart.GetAllDeliveryMethodsResponse;
import io.oigres.ecomm.service.orders.model.cart.InsertCartRequest;
import io.oigres.ecomm.service.orders.model.cart.InsertCartResponse;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.Valid;

public interface DeliveryMethodsService {
    PageResponse<GetAllDeliveryMethodsResponse> getAllDeliveryMethods(@Parameter(hidden = true, required = true) PageableRequestImpl pageable);
}
