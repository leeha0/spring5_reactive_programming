package chapter2.section4.temperature;

import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureSensor {
    private final Random rnd = new Random();

    private final Observable<Temperature> dataStream =
            Observable
                    .range(0, Integer.MAX_VALUE)
                    .concatMap(tick -> Observable
                            .just(tick)
                            .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS)
                            .map(tickValue -> this.probe()))
                    .publish() // 이벤트를 모든 대상 스트림으로 브로드캐스팅
                    .refCount(); // 적어도 하나 이상의 구독자가 있을 때만 구독 생성

    public Temperature probe() {
        return new Temperature(16 + rnd.nextGaussian() * 10);
    }

    public Observable<Temperature> temperatureStream() {
        return dataStream;
    }
}
