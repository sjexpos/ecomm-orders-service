package io.oigres.ecomm.service.orders.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.oigres.ecomm.service.orders.usecases.carts.cleanUp.CleanUpStockTransactionUseCase;

@Configuration
public class StockTransactionsScheduledCleanUpConfiguration {
    private final CleanUpStockTransactionUseCase cleanUpStockTransactionUseCase;

    public StockTransactionsScheduledCleanUpConfiguration(CleanUpStockTransactionUseCase cleanUpStockTransactionUseCase) {
        this.cleanUpStockTransactionUseCase = cleanUpStockTransactionUseCase;
    }

    @Async
    @Scheduled(cron = "@hourly")
    public void cleanUpStockTransactions() {
        cleanUpStockTransactionUseCase.handle();
    }
}
