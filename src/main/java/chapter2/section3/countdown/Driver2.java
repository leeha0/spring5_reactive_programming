package chapter2.section3.countdown;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Driver2 {
    public static void main(String[] args) {
        CountDownLatch doneSignal = new CountDownLatch(100);
        Executor e = Executors.newCachedThreadPool();

        System.out.println("Start");

        for (int i = 0; i < 100; ++i) // create and start threads
            e.execute(new WorkerRunnable(doneSignal, i));

        System.out.println("End");
    }
}