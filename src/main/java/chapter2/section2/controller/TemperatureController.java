package chapter2.section2.controller;

import chapter2.section2.temperature.Temperature;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
//@Controller: Callable(동기), DeferredResult(비동기) 반환 (Spring3)
public class TemperatureController {
    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @RequestMapping(
            value = "/temperature-stream",
            method = RequestMethod.GET
    )
    public SseEmitter events(HttpServletRequest request) {
        // Spring4.2+ ResponseBodyEmitter, SseEmitter, StreamingResponseBody보
        // StreamingResponseBody: 반환값을 비동기로 전달

        // 이벤트를 보내는 목적
        SseEmitter emitter = new SseEmitter();
        clients.add(emitter);

        // Remove emitter from clients on error or disconnect
        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));
        return emitter;
    }

    @Async
    @EventListener
    public void handleMessage(Temperature temperature) {
        // 이벤트 수신을 위한 핸들러
        List<SseEmitter> deadEmitters = new ArrayList<>();
        // 병렬로 클라이언트에게 비동기 전송
        clients.forEach(emitter -> {
            try {
                emitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (Exception ignore) {
                deadEmitters.add(emitter);
            }
        });
        clients.removeAll(deadEmitters);
    }
}
