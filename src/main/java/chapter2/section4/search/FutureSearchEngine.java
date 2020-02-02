package chapter2.section4.search;


import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FutureSearchEngine {
    // 비동기성 확보하였지만 전체 결과를 리턴해야함
    CompletableFuture<List<URL>> search(String query, int limit);
}
