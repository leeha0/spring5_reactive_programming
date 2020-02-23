package chapter4;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.LongConsumer;

@Slf4j
public class MonoEx {
    public static void main(String[] args) {
        Mono
                .fromCallable(() -> 1)
                .log()
                .doOnSubscribe(subscription -> log.debug("doOnSubscribe"))
                .doOnRequest(value -> log.debug("doOnRequest"))
                .subscribeOn(Schedulers.newSingle("sub")) // 부모 Publisher에 대한 구독을 Runnable에서 실행
                .filter(integer -> {
                    log.debug("filter()");
                    return integer.equals(1);
                })
                .publishOn(Schedulers.newSingle("pub"))
                .map(integer -> { // sub-1 -> pub-2
                    log.debug("map()");
                    return 100;
                })
                .doOnNext(integer -> {
                    log.debug("doOnNext: {}", integer);
                })
                .subscribe();
    }
}
