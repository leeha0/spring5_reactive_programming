package chapter1;

import java.util.concurrent.ExecutionException;

public class Future {
    // Future를 통한 리액티브 지원

    interface ShoppingCardService {
        java.util.concurrent.Future<Long> calculate(Long value);
    }

    class OrderService {
        private ShoppingCardService scService;

        void process() throws ExecutionException, InterruptedException {
            Long input = 1L;
            java.util.concurrent.Future<Long> future = scService.calculate(input); // Async & Non-Blocking

            // ...
            Long output = future.get(); // Blocking or 즉시 결과 반환
            // ...
        }
    }
}
