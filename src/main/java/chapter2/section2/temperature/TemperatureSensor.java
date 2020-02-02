package chapter2.section2.temperature;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class TemperatureSensor {
    private final ApplicationEventPublisher publisher;
    private final Random rnd = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    // 빈이 생성될 때 스프링 프레임워크에 의해 호출돼 온도 시나리오의 전체 스퀀스를 시작
    public void startProcessing() {
        this.executor.schedule(this::probe, 1, SECONDS);
    }

    private void probe() {
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));
        // 랜덤한 지연시간(0~5초)을 두고 다음 읽기 스케줄을 예약
        executor.schedule(this::probe, rnd.nextInt(5000), MILLISECONDS);
    }
}
