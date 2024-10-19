package io.oigres.ecomm.service.orders;

import io.oigres.ecomm.service.orders.config.StockTransactionsScheduledCleanUpConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

@SpringBootApplication
@ComponentScan( basePackageClasses = { Bootstrap.class } )
public class Bootstrap {
	private static final String JOB_NAME_PARAMETER = "runjob";
    
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Bootstrap.class);
		if (StringUtils.hasText(System.getenv(JOB_NAME_PARAMETER))) {
			application.setWebApplicationType(WebApplicationType.NONE);
			ConfigurableApplicationContext appCtx = application.run(args);
			if ( StockTransactionsScheduledCleanUpConfiguration.JOB_NAME.equals(System.getenv(JOB_NAME_PARAMETER))) {
				StockTransactionsScheduledCleanUpConfiguration bean = appCtx.getBean(StockTransactionsScheduledCleanUpConfiguration.class);
				bean.cleanUpStockTransactions();
			}
			System.exit(0);
		}
		application.run(args);

	}

}
