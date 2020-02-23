package chapter4;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FluxScEx {
    public static void main(String[] args) {
        Flux.range(0, 10)
                .publishOn(Schedulers.newSingle("pub"))
                .log()
                .subscribeOn(Schedulers.newSingle("sub"))
                .subscribe(integer -> log.debug("accept: {}", integer));

        log.debug("exit");
    }
}
