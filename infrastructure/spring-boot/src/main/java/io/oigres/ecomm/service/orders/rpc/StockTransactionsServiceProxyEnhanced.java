package io.oigres.ecomm.service.orders.rpc;

import io.github.resilience4j.retry.annotation.Retry;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.products.model.exception.NoStockException;
import io.oigres.ecomm.service.products.model.exception.NotFoundException;
import io.oigres.ecomm.service.products.model.exception.StockTimeOutException;
import io.oigres.ecomm.service.products.model.stockTransactions.*;
import io.oigres.ecomm.service.users.api.model.GetUserResponse;

import java.util.List;

public class StockTransactionsServiceProxyEnhanced implements StockTransactionsService {

    private final StockTransactionsService delegate;

    public StockTransactionsServiceProxyEnhanced(StockTransactionsService delegate) {
        this.delegate = delegate;
    }

    @Override
    @Retry(name = "remote-insert-stock-transactions")
    public InsertStockTransactionResponse insertStockTransactions(InsertStockTransactionRequest request) throws NotFoundException, NoStockException, StockTimeOutException {
        return delegate.insertStockTransactions(request);
    }

    @Override
    @Retry(name = "remote-200-expected")
    public void confirmStockTransactions(ConfirmStockTransactionRequest request) {
        delegate.confirmStockTransactions(request);
    }

    @Override
    @Retry(name = "remote-200-expected")
    public void revertStockTransactions(RevertStockTransactionRequest request) {
        delegate.revertStockTransactions(request);
    }

    @Override
    @Retry(name = "remote-200-expected", fallbackMethod = "fallbackMethodForGetElderStockTransactions")
    public GetElderStockTransactionsResponse getElderStockTransactions() {
        return delegate.getElderStockTransactions();
    }

    private GetElderStockTransactionsResponse fallbackMethodForGetElderStockTransactions(Exception e) {
        return GetElderStockTransactionsResponse.builder()
                .stockTransactionIds(List.of())
                .build();
    }

}
