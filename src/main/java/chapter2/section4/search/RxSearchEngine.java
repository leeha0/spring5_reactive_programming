package chapter2.section4.search;

import rx.Observable;

import java.net.URL;

public interface RxSearchEngine {
    // 비동기성과 전체 결과를 리턴하는 단점을 모두 보완
    Observable<URL> search(String query);
}
