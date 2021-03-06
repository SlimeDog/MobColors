package dev.ratas.mobcolors.scheduling;

public interface LongTask {

    void performAtomicPart();

    boolean isDone();

    long getNumberOfParts();

    long getNumberOfPartsDone();

    void onComplete();

    double getUpdateProgress();

    void tickUpdate();

    void onUpdateTime();

}
