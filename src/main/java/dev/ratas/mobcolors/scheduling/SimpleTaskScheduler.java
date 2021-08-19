package dev.ratas.mobcolors.scheduling;

import java.util.ArrayDeque;
import java.util.Queue;

public class SimpleTaskScheduler implements TaskScheduler, Runnable {
    private final Queue<LongTask> queue = new ArrayDeque<>();
    private long maxMsPerTick;
    private long atomicPartsDone = 0L; // for debug / statistics
    private long ticksWorkedOn = 0L; // for debug / statistics
    private long totalTasksDone = 0L; // for debug / statistics
    private long timeSpentOnTasks = 0L; // for debug / statistics

    public SimpleTaskScheduler(long maxMsPerTick) {
        this.maxMsPerTick = maxMsPerTick;
    }

    @Override
    public void run() {
        if (queue.isEmpty()) {
            return; // nothing to do
        }
        long start = System.currentTimeMillis();
        long stop = start + maxMsPerTick;
        while (!queue.isEmpty() && System.currentTimeMillis() < stop) {
            LongTask task = queue.peek();
            task.performAtomicPart();
            if (task.isDone()) {
                queue.poll().onComplete();
                totalTasksDone++;
            }
            atomicPartsDone++;
        }
        if (!queue.isEmpty()) {
            queue.peek().tickUpdate();
        }
        long spent = System.currentTimeMillis() - start;
        timeSpentOnTasks += spent;
        ticksWorkedOn++;
    }

    @Override
    public long getMaxMillisecondsPerTick() {
        return maxMsPerTick;
    }

    @Override
    public void setMaxMillisecondsPerTick(long max) {
        this.maxMsPerTick = max;
    }

    @Override
    public void scheduleTask(LongTask task) {
        queue.add(task);
    }

    public long getTimeSpentOnTasks() {
        return timeSpentOnTasks;
    }

    public long getNumberOfAtomicPartsDone() {
        return atomicPartsDone;
    }

    public long getNumberOfTicksWorkedOn() {
        return ticksWorkedOn;
    }

    public long getNumberOfTasksDone() {
        return totalTasksDone;
    }

    @Override
    public String toString() {
        return String.format("(TaskScheduler\t%d\t%d\t%d\t%d", totalTasksDone, ticksWorkedOn, atomicPartsDone,
                timeSpentOnTasks);
    }

}
