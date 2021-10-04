package dev.ratas.mobcolors.region.version.mock;

import java.util.function.BooleanSupplier;

import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class MockScheduler extends Scheduler {

    public MockScheduler() {
        super(null, null);
    }

    @Override
    public void schedule(Runnable runnable) {
        schedule(runnable, 1);
    }

    @Override
    public void schedule(Runnable runnable, long ticks) {

    }

    @Override
    public void scheduleRepeating(BooleanSupplier supplier, long delay, long period) {

    }

    @Override
    public void scheduleRepeating(Runnable runnable, long delay, long period) {

    }

    @Override
    public void scheduleAsync(Runnable runnable) {

    }

    @Override
    public void scheduleAsync(Runnable runnable, long ticks) {

    }

    @Override
    public void scheduleRepeatingAsync(BooleanSupplier supplier, long delay, long period) {

    }

}
