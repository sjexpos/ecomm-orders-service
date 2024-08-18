package io.oigres.ecomm.service.orders.usecases.paymentMethods.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.domain.PaymentMethod;
import io.oigres.ecomm.service.orders.repository.PaymentMethodRepository;

@Component
public class GetAllPaymentMethodsUseCaseImpl implements GetAllPaymentMethodsUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    public GetAllPaymentMethodsUseCaseImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public Page<PaymentMethod> handle(Pageable pageable) {
        return paymentMethodRepository.findAll(pageable);
    }
}
