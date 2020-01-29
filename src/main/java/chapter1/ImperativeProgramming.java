package chapter1;

public class ImperativeProgramming {
    // 자바 코드를 작성하는 가장 보편적인 기법인 명령형 프로그래밍에서 리액티브 설계 원칙을 따르는가?

    interface ShoppingCardService {
        Long calculate(Long value);
    }

    class OrderService {
        private ShoppingCardService scService;

        void process() {
            Long input = 1L;
            Long output = scService.calculate(input); // Sync & Blocking 동작하며, 리액티브 관점에서 허용하지 않음

            //
        }
    }
}
