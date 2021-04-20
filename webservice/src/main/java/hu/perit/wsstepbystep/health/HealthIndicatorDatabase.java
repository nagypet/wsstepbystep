package hu.perit.wsstepbystep.health;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

import hu.perit.spvitamin.core.timeoutlatch.TimeoutLatch;
import hu.perit.spvitamin.spring.config.SysConfig;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import hu.perit.spvitamin.spring.config.Constants;
import hu.perit.spvitamin.spring.metrics.AsyncExecutor;
import hu.perit.wsstepbystep.db.bookstore.repo.NativeQueryRepo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@DependsOn("metricsProperties")
public class HealthIndicatorDatabase extends AbstractHealthIndicator {
    @Autowired
    private NativeQueryRepo nativeQueryRepo;

    private TimeoutLatch timeoutLatch;

    @PostConstruct
    private void PostConstruct() {
        this.timeoutLatch = new TimeoutLatch(SysConfig.getMetricsProperties().getMetricsGatheringHysteresisMillis());
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        LocalDateTime timestamp = LocalDateTime.now();
        builder.withDetail("Timestamp",
                timestamp.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_JACKSON_TIMESTAMPFORMAT)));

        if (this.timeoutLatch.isClosed()) {
            log.info("Health check failed: the Database server was down, waiting some time before checking it again.");
            builder.down();
            //builder.withDetail("Status", String.format("Database server was down, waiting %d ms to elapse before checking it again.", this.timeoutLatch.millisToWait()));
            builder.withDetail("Status", "Database server was down, waiting some time before checking it again.");
            return;
        }

        try {
            boolean serviceUpAndRunning = AsyncExecutor.invoke(this::checkDbUpAndRunning, false);
            if (serviceUpAndRunning) {
                builder.up();
                builder.withDetail("Status", "Database server is up and running");
            }
            else {
                log.error("Health check failed: the database server is down!");
                builder.down();
                builder.withDetail("Status", "Database server is down!");
            }
        } catch (RuntimeException ex) {
            this.timeoutLatch.setClosed();
            log.error(String.format("Health check failed: %s", ex));
            builder.down();
            builder.withException(ex);
        } catch (TimeoutException ex) {
            this.timeoutLatch.setClosed();
            log.error("Health check failed: the database server cannot be reached (timeout)!");
            builder.down();
            builder.withDetail("Status", "Database server cannot be reached (timeout)!");
        }
    }


    private boolean checkDbUpAndRunning()
    {
        Object result = this.nativeQueryRepo.getSingleResult("SELECT 1", false);
        return result instanceof Integer && ((Integer) result).equals(1);
    }
}
