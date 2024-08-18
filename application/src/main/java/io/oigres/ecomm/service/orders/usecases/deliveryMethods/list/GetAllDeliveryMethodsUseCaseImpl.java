package io.oigres.ecomm.service.orders.usecases.deliveryMethods.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.orders.domain.DeliveryMethod;
import io.oigres.ecomm.service.orders.domain.PaymentMethod;
import io.oigres.ecomm.service.orders.repository.DeliveryMethodRepository;
import io.oigres.ecomm.service.orders.repository.PaymentMethodRepository;

@Component
public class GetAllDeliveryMethodsUseCaseImpl implements GetAllDeliveryMethodsUseCase {
    private final DeliveryMethodRepository deliveryMethodRepository;

    public GetAllDeliveryMethodsUseCaseImpl(DeliveryMethodRepository deliveryMethodRepository) {
        this.deliveryMethodRepository = deliveryMethodRepository;
    }

    @Override
    public Page<DeliveryMethod> handle(Pageable pageable) {
        return deliveryMethodRepository.findAll(pageable);
    }
}
