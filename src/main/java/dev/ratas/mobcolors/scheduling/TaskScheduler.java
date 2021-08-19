package dev.ratas.mobcolors.scheduling;

public interface TaskScheduler {

    long getMaxMillisecondsPerTick();

    void setMaxMillisecondsPerTick(long max);

    void scheduleTask(LongTask task);

}
