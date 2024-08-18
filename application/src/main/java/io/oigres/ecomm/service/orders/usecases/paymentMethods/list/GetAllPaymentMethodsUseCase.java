package io.oigres.ecomm.service.orders.usecases.paymentMethods.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.orders.domain.PaymentMethod;

public interface GetAllPaymentMethodsUseCase {
    Page<PaymentMethod> handle(Pageable pageable);
}
