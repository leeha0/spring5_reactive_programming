package chapter2.section3;

import rx.Observable;
import rx.Subscriber;

public class Application {
    public static void main(String[] args) {
        // Observable은 배압 기능이 없기 때문에 사용하지 않는 것을 권장
        Observable<String> observable = Observable.create(
                // ObservableOnSubscribe
                sub -> {
                    sub.onNext("Hello, reactive world!");
                    sub.onCompleted();
                }
        );

        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
                System.out.println("Done!");

            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t);
            }
        };

        observable.subscribe(subscriber);

        Observable.create(
                // ObservableOnSubscribe
                sub -> {
                    sub.onNext("Hello, reactive world!");
                    sub.onCompleted();
                }
        ).subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("Done!")
        );
    }
}

