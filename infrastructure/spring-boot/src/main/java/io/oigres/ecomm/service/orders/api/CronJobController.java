package io.oigres.ecomm.service.orders.api;

import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.config.StockTransactionsScheduledCleanUpConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Routes.CRON_JOBS_CONTROLLER_PATH)
@Tag(name = "Cron jobs", description = " ")
@Slf4j
@Validated
@ConditionalOnProperty(prefix = "ecomm.service.orders.scheduling", name="enabled", havingValue="false", matchIfMissing = false)
public class CronJobController {
    private final StockTransactionsScheduledCleanUpConfiguration stockTransactionsScheduledCleanUpConfiguration;

    public CronJobController(StockTransactionsScheduledCleanUpConfiguration stockTransactionsScheduledCleanUpConfiguration) {
        this.stockTransactionsScheduledCleanUpConfiguration = stockTransactionsScheduledCleanUpConfiguration;
    }

    @Operation(summary = "Run job")
    @PostMapping(produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> runJob(@RequestBody @Valid String jobName) {
        if (StockTransactionsScheduledCleanUpConfiguration.JOB_NAME.equals(jobName)) {
            this.stockTransactionsScheduledCleanUpConfiguration.cleanUpStockTransactions();
            return ResponseEntity.ok("DONE");
        }
        return ResponseEntity.badRequest().body("Unknown job");
    }

}
