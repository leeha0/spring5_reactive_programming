package chapter1;

import java.util.function.Consumer;

public class Callback {
    // 콜백 기법을 적용하여 리액티브 지원

    interface ShoppingCardService {
        void calculate(Long value, Consumer<Long> c);
    }

    class OrderService {
        private ShoppingCardService scService;

        void process() {
            Long input = 1L;
            scService.calculate(input, output -> { // Async & Non-Blocking, 콜백 함수 호출 방식에 따라 동기/비동기 구현 가능
                // ...
            });
        }
    }

    class SyncShoppingCardService implements ShoppingCardService {

        @Override
        public void calculate(Long value, Consumer<Long> c) {
            Long result = 1L;
            c.accept(result); // 즉시 결과 전달
        }
    }

    class AsyncShoppingCardService implements ShoppingCardService {

        @Override
        public void calculate(Long value, Consumer<Long> c) {
            new Thread(() -> {
                // 작업
                c.accept(1L); // 별도의 Thread에서 처리하여 결고 전달
            });
        }
    }
}
