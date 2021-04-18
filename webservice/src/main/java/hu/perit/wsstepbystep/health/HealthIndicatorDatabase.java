package hu.perit.wsstepbystep.health;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import hu.perit.spvitamin.spring.config.Constants;
import hu.perit.spvitamin.spring.metrics.AsyncExecutor;
import hu.perit.wsstepbystep.db.postgres.repo.NativeQueryRepo;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HealthIndicatorDatabase extends AbstractHealthIndicator
{
    @Autowired
    private NativeQueryRepo nativeQueryRepo;

    private TimeoutLatch timeoutLatch = new TimeoutLatch();

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception
    {
        LocalDateTime timestamp = LocalDateTime.now();
        builder.withDetail("Timestamp", timestamp.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_JACKSON_TIMESTAMPFORMAT)));

        try
        {
            Boolean dbUpAndRunning = AsyncExecutor.invoke(this::checkDbUpAndRunning, null);
            if (BooleanUtils.isTrue(dbUpAndRunning))
            {
                builder.up();
                builder.withDetail("Status", "Database server is up and running");
            }
            else
            {
                builder.down();
                builder.withDetail("Status", "Database server cannot be reached!");
            }
        }
        catch (RuntimeException ex)
        {
            log.error(String.format("Health check failed: %s", ex));
            builder.down();
            builder.withDetail("error", ex);
        }
        catch (TimeoutException ex)
        {
            this.timeoutLatch.setClosed();
            log.error("Health check failed: the database server cannot be reached!");
            builder.down();
            builder.withDetail("Status", "Database server cannot be reached!");
        }
    }


    private Boolean checkDbUpAndRunning()
    {
        if (this.timeoutLatch.isClosed())
        {
            return null;
        }

        Object result = this.nativeQueryRepo.getSingleResult("SELECT 1", false);
        return result instanceof Integer && ((Integer) result).equals(1);
    }
}
