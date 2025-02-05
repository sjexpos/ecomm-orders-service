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

package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.config.StockTransactionsScheduledCleanUpConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Bootstrap.class})
public class Bootstrap {
  private static final String JOB_NAME_PARAMETER = "runjob";

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(Bootstrap.class);
    if (StringUtils.hasText(System.getenv(JOB_NAME_PARAMETER))) {
      application.setWebApplicationType(WebApplicationType.NONE);
      ConfigurableApplicationContext appCtx = application.run(args);
      if (StockTransactionsScheduledCleanUpConfiguration.JOB_NAME.equals(
          System.getenv(JOB_NAME_PARAMETER))) {
        StockTransactionsScheduledCleanUpConfiguration bean =
            appCtx.getBean(StockTransactionsScheduledCleanUpConfiguration.class);
        bean.cleanUpStockTransactions();
      }
      System.exit(0);
    }
    application.run(args);
  }
}
