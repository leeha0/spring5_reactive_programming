package chapter2.section3;

import rx.Observable;
import rx.Subscription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UnsubscribeApplication {
    public static void main(String[] args) throws InterruptedException {
        // countDown() 메소드에 의해 0에 도달할 때까지 대기
        CountDownLatch externalSignal = new CountDownLatch(1);

        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

        externalSignal.await();
        subscription.unsubscribe();
    }
}
