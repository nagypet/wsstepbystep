package hu.perit.wsstepbystep.metrics;

import java.util.List;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Service;

import hu.perit.wsstepbystep.config.Constants;
import hu.perit.wsstepbystep.health.HealthIndicatorDatabase;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;


@Service
@Getter
public class MicrometerMetricsService
{
    private final List<AbstractHealthIndicator> indicators;


    public MicrometerMetricsService(MeterRegistry registry, MetricsProviderService myMetricProvider, HealthIndicatorDatabase healthIndicatorDatabase)
    {
        final String METRIC_BOOK_COUNT = Constants.SUBSYSTEM_NAME.toLowerCase() + ".bookcount";
        final String METRIC_HEALTH = Constants.SUBSYSTEM_NAME.toLowerCase() + ".health";

        Gauge.builder(METRIC_BOOK_COUNT, myMetricProvider, MetricsProviderService::getBookCount).description(
            "The current count of books").baseUnit("pcs").register(registry);

        indicators = List.of(healthIndicatorDatabase);
        Gauge.builder(METRIC_HEALTH, indicators, MicrometerMetricsService::healthToCode).description(
            "The current value of the health endpoint").register(registry);
    }


    private static int healthToCode(List<AbstractHealthIndicator> indicators)
    {
        for (AbstractHealthIndicator indicator : indicators)
        {
            Status status = indicator.health().getStatus();
            if (status.equals(Status.DOWN))
            {
                return 0;
            }
        }

        return 1;
    }
}
