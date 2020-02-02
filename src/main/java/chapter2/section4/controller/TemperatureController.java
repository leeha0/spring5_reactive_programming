package chapter2.section4.controller;

import chapter2.section4.emitter.RxSseEmitter;
import chapter2.section4.temperature.TemperatureSensor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TemperatureController {
    private final TemperatureSensor temperatureSensor;

    public TemperatureController(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    @RequestMapping(
            value = "/temperature-stream",
            method = RequestMethod.GET
    )
    public SseEmitter events(HttpServletRequest request) {
        RxSseEmitter emitter = new RxSseEmitter();

        // 구독 해지 관리 및 동기화를 신경 쓰지 않음
        // 이벤트 버스를 사용하지 않음으로 이식성이 높고 테스트가 가능한 코드
        temperatureSensor.temperatureStream()
                .subscribe(emitter.getSubscriber());

        return emitter;
    }
}
