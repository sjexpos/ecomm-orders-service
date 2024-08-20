package io.oigres.ecomm.service.orders.api;

import io.oigres.ecomm.service.orders.CartsService;
import io.oigres.ecomm.service.orders.DeliveryMethodsService;
import io.oigres.ecomm.service.orders.PaymentMethodsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.domain.DeliveryMethod;
import io.oigres.ecomm.service.orders.domain.PaymentMethod;
import io.oigres.ecomm.service.orders.exception.CreateCartException;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageResponseImpl;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.cart.*;
import io.oigres.ecomm.service.orders.model.carts.InsertCartRequestDTO;
import io.oigres.ecomm.service.orders.model.carts.InsertCartResponseDTO;
import io.oigres.ecomm.service.orders.model.carts.InsertPublicationWithAmountRequestDTO;
import io.oigres.ecomm.service.orders.usecases.carts.create.CreateCartUseCase;
import io.oigres.ecomm.service.orders.usecases.deliveryMethods.list.GetAllDeliveryMethodsUseCase;
import io.oigres.ecomm.service.orders.usecases.paymentMethods.list.GetAllPaymentMethodsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Routes.CARTS_CONTROLLER_PATH)
@Tag(name = "Carts", description = " ")
@Slf4j
@Validated
public class CartsController extends AbstractController implements CartsService, PaymentMethodsService, DeliveryMethodsService {
    private final GetAllPaymentMethodsUseCase getAllPaymentMethodsUseCase;
    private final GetAllDeliveryMethodsUseCase getAllDeliveryMethodsUseCase;
    private final CreateCartUseCase createCartUseCase;

    public CartsController(GetAllPaymentMethodsUseCase getAllPaymentMethodsUseCase, GetAllDeliveryMethodsUseCase getAllDeliveryMethodsUseCase, CreateCartUseCase createCartUseCase) {
        this.getAllPaymentMethodsUseCase = getAllPaymentMethodsUseCase;
        this.getAllDeliveryMethodsUseCase = getAllDeliveryMethodsUseCase;
        this.createCartUseCase = createCartUseCase;
    }

    @Override
    @Operation(summary = "Create Cart")
    @PostMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public InsertCartResponse createCart(@RequestBody @Valid InsertCartRequest request)
            throws io.oigres.ecomm.service.orders.model.NotFoundException, io.oigres.ecomm.service.orders.model.NoStockException, io.oigres.ecomm.service.orders.model.StockTimeOutException,
            io.oigres.ecomm.service.orders.model.CreateCartException {
        log.info("############ call createCart ############");
        InsertCartResponseDTO response;
        try {
            response = createCartUseCase.handle(InsertCartRequestDTO.builder().userId(request.getUserId()).publications(
                    request.getPublications().stream().map(pub -> InsertPublicationWithAmountRequestDTO.builder().publicationId(pub.getPublicationId()).amount(pub.getAmount()).build())
                            .collect(Collectors.toList())).build());
        } catch (io.oigres.ecomm.service.products.model.exception.NotFoundException | io.oigres.ecomm.service.users.api.model.exception.NotFoundException | io.oigres.ecomm.service.orders.exception.NotFoundException e) {
            throw new io.oigres.ecomm.service.orders.model.NotFoundException(e.getMessage());
        } catch (io.oigres.ecomm.service.products.model.exception.NoStockException e) {
            throw new io.oigres.ecomm.service.orders.model.NoStockException(e.getMessage());
        } catch (io.oigres.ecomm.service.products.model.exception.StockTimeOutException e) {
            throw new io.oigres.ecomm.service.orders.model.StockTimeOutException(e.getMessage());
        } catch (CreateCartException e) {
            throw new io.oigres.ecomm.service.orders.model.CreateCartException(e.getMessage());
        }
        return InsertCartResponse.builder().orders(response.getOrders().stream().map(or -> InsertOrderResponse.builder().orderId(or.getOrderId()).dispensaryId(or.getDispensaryId()).publications(
                        or.getPublications().stream()
                                .map(pub -> InsertOrderDispensaryPublicationResponse.builder().dispensaryPublicationId(pub.getPublicationId()).amount(pub.getAmount()).price(pub.getPrice()).build())
                                .collect(Collectors.toList())).status(or.getStatus()).deliveryMethod(or.getDeliveryMethod()).paymentMethod(or.getPaymentMethod()).subtotal(or.getSubtotal())
                .exciseTax(or.getExciseTax()).salesTax(or.getSalesTax()).total(or.getTotal()).build()).collect(Collectors.toList())).build();
    }

    @Override
    @Operation(summary = "Get all payment methods")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_PAYMENT_METHODS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GetAllPaymentMethodsResponse> getAllPaymentMethods(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.debug("############ call getAllPaymentMethods ############");
        Page<PaymentMethod> paymentMethods = getAllPaymentMethodsUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<GetAllPaymentMethodsResponse> response =
                paymentMethods.getContent().stream().map(el -> GetAllPaymentMethodsResponse.builder().id(el.getId()).name(el.getName().getPrettyName()).build()).collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, paymentMethods.getTotalElements());
    }

    @Override
    @Operation(summary = "Get all delivery methods")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_DELIVERY_METHODS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GetAllDeliveryMethodsResponse> getAllDeliveryMethods(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.debug("############ call getAllDeliveryMethods ############");
        Page<DeliveryMethod> deliveryMethods = getAllDeliveryMethodsUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<GetAllDeliveryMethodsResponse> response =
                deliveryMethods.getContent().stream().map(el -> GetAllDeliveryMethodsResponse.builder().id(el.getId()).name(el.getName().getPrettyName()).build()).collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, deliveryMethods.getTotalElements());
    }
}
