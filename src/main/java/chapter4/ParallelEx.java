package chapter4;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ParallelEx {
    public static void main(String[] args) {
        Flux.range(0, 100)
                .log()
                .parallel()
                .runOn(Schedulers.parallel())
                .map(integer -> integer * 2)
                .filter(integer -> integer % 2 == 0)
                .subscribe();

        log.debug("exit");
    }
}
