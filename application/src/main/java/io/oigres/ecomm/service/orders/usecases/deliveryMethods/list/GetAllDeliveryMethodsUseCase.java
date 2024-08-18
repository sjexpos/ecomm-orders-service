package io.oigres.ecomm.service.orders.usecases.deliveryMethods.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.orders.domain.DeliveryMethod;
import io.oigres.ecomm.service.orders.domain.PaymentMethod;

public interface GetAllDeliveryMethodsUseCase {
    Page<DeliveryMethod> handle(Pageable pageable);
}
