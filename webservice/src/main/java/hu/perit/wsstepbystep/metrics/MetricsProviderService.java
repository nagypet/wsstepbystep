package hu.perit.wsstepbystep.metrics;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.perit.spvitamin.spring.config.SysConfig;
import hu.perit.spvitamin.spring.metrics.AsyncExecutor;
import hu.perit.wsstepbystep.db.bookstore.repo.BookRepo;
import hu.perit.wsstepbystep.health.TimeoutLatch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MetricsProviderService
{
    @Autowired
    private BookRepo bookRepo;

    private TimeoutLatch timeoutLatch = new TimeoutLatch();

    public double getBookCount()
    {
        try
        {
            long bookCount = AsyncExecutor.invoke(this::getTotalBookCount, null);
            return (double) bookCount;
        }
        catch (TimeoutException ex)
        {
            this.timeoutLatch.setClosed();
            log.error(String.format("getTotalBookCount() did not complete within %d ms! The database is not reachable or slow!",
                SysConfig.getMetricsProperties().getTimeoutMillis()));
        }

        return 0.0;
    }


    private long getTotalBookCount()
    {
        if (this.timeoutLatch.isClosed())
        {
            return 0;
        }

        return bookRepo.count();
    }
}
