package io.oigres.ecomm.service.orders.usecases.carts.cleanUp;

import io.oigres.ecomm.service.orders.domain.OrderProduct;
import io.oigres.ecomm.service.orders.repository.OrderProductRepository;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.products.model.stockTransactions.ConfirmStockTransactionRequest;
import io.oigres.ecomm.service.products.model.stockTransactions.GetElderStockTransactionsResponse;
import io.oigres.ecomm.service.products.model.stockTransactions.RevertStockTransactionRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CleanUpStockTransactionUseCaseImpl implements CleanUpStockTransactionUseCase {
    private final StockTransactionsService stockTransactionsService;
    private final OrderProductRepository orderProductRepository;

    public CleanUpStockTransactionUseCaseImpl(StockTransactionsService stockTransactionsService, OrderProductRepository orderProductRepository) {
        this.stockTransactionsService = stockTransactionsService;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public void handle() {
        GetElderStockTransactionsResponse stockTransactions = stockTransactionsService.getElderStockTransactions();
        List<Long> transactionIds = stockTransactions.getStockTransactionIds();
        if (!transactionIds.isEmpty()) {
            List<OrderProduct> orderProducts = orderProductRepository.findByTransactionIdIn(transactionIds);
            Set<Long> set = orderProducts.stream().map(OrderProduct::getTransactionId).collect(Collectors.toSet());
            List<Long> transactionsToConfirm = new ArrayList<>();
            List<Long> transactionsToRevert = new ArrayList<>();
            transactionIds.forEach(tr -> {
                if (set.contains(tr)) {
                    transactionsToConfirm.add(tr);
                } else {
                    transactionsToRevert.add(tr);
                }
            });
            if (!transactionsToConfirm.isEmpty()) {
                stockTransactionsService.confirmStockTransactions(ConfirmStockTransactionRequest.builder().transactionIds(transactionsToConfirm).build());
            }
            if (!transactionsToRevert.isEmpty()) {
                stockTransactionsService.revertStockTransactions(RevertStockTransactionRequest.builder().transactionIds(transactionsToRevert).build());
            }
        }
    }
}
