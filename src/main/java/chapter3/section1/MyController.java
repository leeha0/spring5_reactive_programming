package chapter3.section1;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

@RestController
public class MyController {

    public ListenableFuture<T> requestData() {
//        AsyncRestTemplate httpClient = ;
//        AsyncDatabaseClient databaseClient = ;

        CompletionStage<String> completionStage = AsyncAdepters.toCompletion(
//                httpClient.execute()
        );

        return AsyncAdepters.toListenable(
//                databaseClient.store(completionStage);
        );
    }
}
