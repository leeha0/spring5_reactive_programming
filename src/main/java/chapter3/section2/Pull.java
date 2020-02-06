package chapter3.section2;

import chapter3.section1.AsyncDatabaseClient;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PullModel {
//    final AsyncDatabaseClient dbClient =

    public CompletionStage<Queue<String>> list(int count) {
        BlockingQueue<String> storage = new ArrayBlockingQueue<>(count);
        CompletableFuture<Queue<String>> result = new CompletableFuture<>();

        pull("1", storage, result, count);

        return result;
    }

    void pull(String elementId, Queue<String> queue, CompletableFuture resultFuture, int count) {
        dbClient.getNextAfterId(elementId)
                .thenAccept(item -> {
                    if (isValid(item)) {
                        queue.offer(item);

                        if (queue.size() == count) {
                            resultFuture.complete(queue);
                            return;
                        }
                    }

                    pull(item, queue, resultFuture, count);
                });
    }

}
