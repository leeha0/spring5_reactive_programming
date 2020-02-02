package chapter2.section3.countdown;

import java.util.concurrent.CountDownLatch;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(100);

        System.out.println("Start");

        for (int i = 0; i < 100; ++i) // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();

        // do something else
        // don't let run yet

        startSignal.countDown(); // let all threads proceed

        // do something else

        doneSignal.await(); // wait for all to finish

        System.out.println("End");
    }
}
