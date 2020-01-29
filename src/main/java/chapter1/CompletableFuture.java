package chapter1;

import java.util.concurrent.CompletionStage;

public class CompletableFuture {
    // 결과를 얻기 위해 현재 스레드를 차단하고 혹장성을 저하시키는 외부 실행과 동기화 필요
    // 이를 개선해 CompletionStage를 직접 구현한 CompletableFuture 사용

    interface ShoppingCardService {
        CompletionStage<Long> calculate(Long input);
    }

    class OrderService {
        private ShoppingCardService scService;

        void process() {
            Long input = 1L;

            scService.calculate(input) // Async & Non-Blocking
//                    .thenApply(output -> {})
//                    .thenCombine(output -> {})
                    .thenAccept(System.out::println);
        }
    }
}
