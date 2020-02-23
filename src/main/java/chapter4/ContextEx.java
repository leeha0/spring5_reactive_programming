package chapter4;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class ContextEx {
    public static void main(String[] args) {
        Flux.range(0, 10)
                .take(1)
//                .log()
                .flatMap(k ->
                        Mono.subscriberContext() // 현재 스트림의 Context에 접근
                                .doOnNext(context -> {
                                    Map<Object, Object> map = context.get("randoms");
                                    map.put(k, new Random(k).nextGaussian());
                                    log.debug("map: {}", map);
                                }).thenReturn(k))
                .publishOn(Schedulers.parallel())
                .flatMap(k ->
                        Mono.subscriberContext() // 스레드 변경 후 다시 Context에 엑세스
                                .map(context -> {
                                    Map<Object, Object> map = context.get("randoms");
                                    log.debug("map: {}", map);
                                    return map.get(k); // 저장된 맵에 접근하여 갑 반환
                                }))
                .subscriberContext(context ->
                        context.put("randoms", new HashMap())) // "randoms"를 만들기 위해 새로운 Context 인스턴스 반환
                .blockLast();
    }
}
