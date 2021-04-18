package hu.perit.wsstepbystep.health;

import hu.perit.spvitamin.spring.config.SysConfig;

public class TimeoutLatch
{

    private boolean opened;
    private long timeClosing;

    public TimeoutLatch()
    {
        this.opened = true;
    }


    public synchronized void setClosed()
    {
        this.opened = false;
        this.timeClosing = System.currentTimeMillis();
    }

    public synchronized boolean isOpen()
    {
        if (this.opened)
        {
            return true;
        }

        if ((System.currentTimeMillis() - this.timeClosing) > SysConfig.getMetricsProperties().getMetricsGatheringHysteresisMillis())
        {
            this.opened = true;
            return true;
        }

        return false;
    }
    
    public boolean isClosed()
    {
        return !isOpen();
    }
}
