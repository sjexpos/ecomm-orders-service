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

package io.oigres.ecomm.service.orders.rpc;

import io.github.resilience4j.retry.annotation.Retry;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.products.model.exception.NoStockException;
import io.oigres.ecomm.service.products.model.exception.NotFoundException;
import io.oigres.ecomm.service.products.model.exception.StockTimeOutException;
import io.oigres.ecomm.service.products.model.stockTransactions.*;
import java.util.List;

public class StockTransactionsServiceProxyEnhanced implements StockTransactionsService {

  private final StockTransactionsService delegate;

  public StockTransactionsServiceProxyEnhanced(StockTransactionsService delegate) {
    this.delegate = delegate;
  }

  @Override
  @Retry(name = "remote-insert-stock-transactions")
  public InsertStockTransactionResponse insertStockTransactions(
      InsertStockTransactionRequest request)
      throws NotFoundException, NoStockException, StockTimeOutException {
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
  @Retry(
      name = "remote-200-expected",
      fallbackMethod = "fallbackMethodForGetElderStockTransactions")
  public GetElderStockTransactionsResponse getElderStockTransactions() {
    return delegate.getElderStockTransactions();
  }

  private GetElderStockTransactionsResponse fallbackMethodForGetElderStockTransactions(
      Exception e) {
    return GetElderStockTransactionsResponse.builder().stockTransactionIds(List.of()).build();
  }
}
