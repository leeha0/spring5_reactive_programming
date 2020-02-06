package chapter3.section1;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

final class AsyncAdepters {
    public static <T> CompletionStage<T> toCompletion(ListenableFuture<T> future) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        future.addCallback(
                completableFuture::complete,
                completableFuture::completeExceptionally
        );

        return completableFuture;
    }

    public static <T> ListenableFuture<T> toListenable(CompletionStage<T> stage) {
        SettableListenableFuture<T> future = new SettableListenableFuture<>();

        stage.whenComplete((v, t) -> {
            if (t == null) {
                future.set(v);
            } else {
                future.setException(t);
            }
        });

        return future;
    }
}
