package chapter2.section3;

import rx.Observable;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcatApplication {
    public static void main(String[] args) {
        Observable.just("1", "2", "3", "4");
        Observable.from(new String[]{"A", "B", "C"});
        Observable.from(Collections.emptyList());

        Observable<String> hello = Observable.fromCallable(() -> "Hello ");
        Future<String> future = Executors.newCachedThreadPool().submit(() -> "World");
        Observable<String> world = Observable.from(future);

        Observable.concat(hello, world, Observable.just("!"))
                .forEach(System.out::print);
    }
}
