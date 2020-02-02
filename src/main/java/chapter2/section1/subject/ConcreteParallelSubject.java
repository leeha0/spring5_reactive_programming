package chapter2.section1.subject;

import chapter2.section1.observer.Observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcreteParallelSubject implements Subject<String> {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Set<Observer<String>> observers = new CopyOnWriteArraySet<>();

    @Override
    public void registerObserver(Observer<String> observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event) {
        observers.forEach(observer ->
                executorService.submit(
                        () -> observer.observe(event)
                )
        );
    }
}
