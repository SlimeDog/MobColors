package dev.ratas.mobcolors.scheduling.abstraction;

import java.util.function.BooleanSupplier;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {
    private final Plugin plugin;
    private final BukkitScheduler bukkitScheduler;

    public Scheduler(Plugin plugin, BukkitScheduler bukkitScheduler) {
        this.plugin = plugin;
        this.bukkitScheduler = bukkitScheduler;
    }

    public void schedule(Runnable runnable) {
        schedule(runnable, 1);
    }

    public void schedule(Runnable runnable, long ticks) {
        bukkitScheduler.runTaskLater(plugin, runnable, ticks);
    }

    /**
     * Schedules a repeating task that is first scheduled after {@delay} ticks and
     * then runs every {@period} ticks. If the supplier provides true, the repeating
     * task is cancelled.
     *
     * @param supplier
     * @param delay
     * @param period
     */
    public void scheduleRepeating(BooleanSupplier supplier, long delay, long period) {
        bukkitScheduler.runTaskTimer(plugin, (t) -> {
            if (supplier.getAsBoolean()) {
                t.cancel();
            }
        }, delay, period);
    }

    /**
     * Schedules a repeating task that is first scheduled after {@delay} ticks and
     * then runs every {@period} ticks. This task is (generally) only stopped once
     * the plugin stops its operations.
     *
     * @param runnable
     * @param delay
     * @param period
     */
    public void scheduleRepeating(Runnable runnable, long delay, long period) {
        bukkitScheduler.runTaskTimer(plugin, (t) -> runnable.run(), delay, period);
    }

    public void scheduleAsync(Runnable runnable) {
        scheduleAsync(runnable, 1);
    }

    public void scheduleAsync(Runnable runnable, long ticks) {
        bukkitScheduler.runTaskLaterAsynchronously(plugin, runnable, ticks);
    }

    /**
     * Schedules an asyncrhronous repeating task that is first scheduled after
     * {@delay} ticks and then runs every {@period} ticks. If the supplier provides
     * true, the repeating task is cancelled.
     *
     * @param supplier
     * @param delay
     * @param period
     */
    public void scheduleRepeatingAsync(BooleanSupplier supplier, long delay, long period) {
        bukkitScheduler.runTaskTimerAsynchronously(plugin, (t) -> supplier.getAsBoolean(), delay, period);
    }

}
