package chapter4;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ScheduleEx {
    public static void main(String[] args) {
        // publisher
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    // pool-2-thread-1
                    log.debug("request()");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {
                    // pool-2-thread-1

                }
            });
        };

        // subscribeOn: 느린 생성, 빠른 컨슘
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService ex = Executors.newSingleThreadScheduledExecutor();
            // main -> pool-2-thread-1
            ex.execute(() -> pub.subscribe(sub));
            ex.shutdown();
        };

        // publishOn: 빠른 생성, 느린 컨슘
        Publisher<Integer> pubOnPub = sub -> subOnPub.subscribe(new Subscriber<Integer>() {
            ExecutorService ex = Executors.newSingleThreadScheduledExecutor();

            @Override
            public void onSubscribe(Subscription s) {
                // pool-2-thread-1
                sub.onSubscribe(s);
            }

            @Override
            public void onNext(Integer integer) {
                // pool-2-thread-1 -> pool-1-thread-1
                ex.execute(() -> sub.onNext(integer));
            }

            @Override
            public void onError(Throwable t) {
                // pool-2-thread-1 -> pool-1-thread-1
                ex.execute(() -> sub.onError(t));
                ex.shutdown();
            }

            @Override
            public void onComplete() {
                // pool-2-thread-1 -> pool-1-thread-1
                ex.execute(sub::onComplete);
                ex.shutdown();
            }
        });

        // subscriber
        pubOnPub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                // pool-2-thread-1
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                // pool-1-thread-1
                log.debug("onNext: {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                // pool-1-thread-1
                log.debug("onError: {}", t);
            }

            @Override
            public void onComplete() {
                // pool-1-thread-1
                log.debug("onComplete");
            }
        });

        log.debug("exit");
    }
}
