package chapter2.section4.search;

import java.net.URL;

public interface IterableSearchEngine {
    // 자원낭비를 최호화하였지만 다음 데이터 반환까지 스레드 차단
    Iterable<URL> search(String query, int limit);
}
