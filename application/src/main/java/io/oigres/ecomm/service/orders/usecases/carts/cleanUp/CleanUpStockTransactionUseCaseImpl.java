/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.orders.usecases.carts.cleanUp;

import io.oigres.ecomm.service.orders.domain.OrderProduct;
import io.oigres.ecomm.service.orders.repository.OrderProductRepository;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.products.model.stockTransactions.ConfirmStockTransactionRequest;
import io.oigres.ecomm.service.products.model.stockTransactions.GetElderStockTransactionsResponse;
import io.oigres.ecomm.service.products.model.stockTransactions.RevertStockTransactionRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CleanUpStockTransactionUseCaseImpl implements CleanUpStockTransactionUseCase {
  private final StockTransactionsService stockTransactionsService;
  private final OrderProductRepository orderProductRepository;

  public CleanUpStockTransactionUseCaseImpl(
      StockTransactionsService stockTransactionsService,
      OrderProductRepository orderProductRepository) {
    this.stockTransactionsService = stockTransactionsService;
    this.orderProductRepository = orderProductRepository;
  }

  @Override
  public void handle() {
    log.info("Cleaning up stock transactions");
    GetElderStockTransactionsResponse stockTransactions =
        stockTransactionsService.getElderStockTransactions();
    List<Long> transactionIds = stockTransactions.getStockTransactionIds();
    if (!transactionIds.isEmpty()) {
      List<OrderProduct> orderProducts =
          orderProductRepository.findByTransactionIdIn(transactionIds);
      Set<Long> set =
          orderProducts.stream().map(OrderProduct::getTransactionId).collect(Collectors.toSet());
      List<Long> transactionsToConfirm = new ArrayList<>();
      List<Long> transactionsToRevert = new ArrayList<>();
      transactionIds.forEach(
          tr -> {
            if (set.contains(tr)) {
              transactionsToConfirm.add(tr);
            } else {
              transactionsToRevert.add(tr);
            }
          });
      if (!transactionsToConfirm.isEmpty()) {
        stockTransactionsService.confirmStockTransactions(
            ConfirmStockTransactionRequest.builder().transactionIds(transactionsToConfirm).build());
      }
      if (!transactionsToRevert.isEmpty()) {
        stockTransactionsService.revertStockTransactions(
            RevertStockTransactionRequest.builder().transactionIds(transactionsToRevert).build());
      }
      log.info(
          "{} transactions were confirmed and {} were reverted.",
          transactionsToConfirm.size(),
          transactionsToRevert.size());
    }
  }
}
