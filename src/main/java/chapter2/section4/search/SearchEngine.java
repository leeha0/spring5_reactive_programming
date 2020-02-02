package chapter2.section4.search;

import java.net.URL;
import java.util.List;

public interface SearchEngine {
    // 동기식으로 동작하며 전체 결과를 리턴받기 떄문에 자원낭비
    List<URL> search(String query, int limit);
}
