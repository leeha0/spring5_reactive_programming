package chapter4;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class FluxEx {
    public static void main(String[] args) throws InterruptedException {
        Flux.interval(Duration.ofMillis(200)).take(10)
                .subscribe(s -> log.debug("onNext: {}", s));

        Thread.sleep(1000);
    }
}
