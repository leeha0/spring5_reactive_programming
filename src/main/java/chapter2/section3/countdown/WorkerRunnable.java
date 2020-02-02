package chapter2.section3.countdown;

import java.util.concurrent.CountDownLatch;

public class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;

    public WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    public void run() {
        doWork(i);
        doneSignal.countDown();
    }

    void doWork(int i) {
        System.out.println("Do Work!");
    }
}
