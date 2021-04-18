package hu.perit.wsstepbystep.health;

import hu.perit.spvitamin.spring.config.SysConfig;

class TimeoutLatch
{

    private boolean opened;
    private long timeClosing;

    TimeoutLatch()
    {
        this.opened = true;
    }


    synchronized void setClosed()
    {
        this.opened = false;
        this.timeClosing = System.currentTimeMillis();
    }

    synchronized boolean isOpen()
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
    
    boolean isClosed()
    {
        return !isOpen();
    }
}
