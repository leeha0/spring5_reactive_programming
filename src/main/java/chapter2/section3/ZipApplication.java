package chapter2.section3;

import rx.Observable;

public class ZipApplication {
    public static void main(String[] args) {
        Observable.zip(
                Observable.just("A", "B", "C"),
                Observable.just("1", "2", "3"),
                (x, y) -> x + y
        ).forEach(System.out::println);
    }
}
