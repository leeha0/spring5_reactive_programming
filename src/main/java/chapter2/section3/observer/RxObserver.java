package chapter2.section3.observer;

public interface RxObserver<T> {
    void onNext(T next);
    void onComplete();
    void onError(Exception e);
}
