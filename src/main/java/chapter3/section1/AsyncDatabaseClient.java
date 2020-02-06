package chapter3.section1;

import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {
    <T> CompletionStage<T> store(CompletionStage<T> stage);
}
