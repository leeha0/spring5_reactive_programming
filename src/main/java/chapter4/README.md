# 리액터 프로젝트 - 리액티브 앱의 기초
* 리액티브 스트림 스펙
* 푸시-풀 모델

### 리액터 프로젝트의 간략한 역사
* 리액티브 스트림 스펙은 리액티브 라이브러리가 서로 호환하도록 기준 정의
* 풀-푸시 데이터 교환 모델을 도입해 배압 문제 해결

### 리액터 프로젝트 버전 1.x
* 처리량이 많은 데이터 처리를 위한 프레임워크 필요성 증가  
    → 리택터 프로젝트 시작
* 리액터 패턴, 함수형 프로그래밍, 리액티브 프로그래밍과 같은 메시지 처리 기술 통합
* 스프링 프레임워크와 완벽한 통합 및 네티와의 결합을 통한 비동기 및 논블로킹 메시지 처리 제공
* 단점
    * 배압 기능 없음
    * 에러 처리 복잡성

> 리액터 패턴
> * 비동기 이벤트 처리 및 동기 처리에 도움이 되는 행위 패턴
> * 모든 이벤트는 큐에 추가되며, 이벤트는 별도의 스레드에서 동기적 처리

#### 리액터 프로젝트 버전 2.x
* 리액티브 스트림 스펙을 준수하는 핵심 모듈 제공  
    → 배압 관리, 스레드 처리, 복원력 지원 등 
* 이벤트 버스 및 스트림 기능 별도 모듈로 제공
* 자바 컬렉션 API와 쉽게 통합

#### 리액터 프로젝트 버전 2.5 ~ 3.0
* reactive-stream-commons 라이브러리   
    → 2.5 ~ 3.0 버전의 기초
* 자바 8 기준
* 스프링 프레임워크 5의 리액티브적인 변경 담당

### 리액터 프로젝트 필수 요소
* 콜백 지옥과 깊이 중첩된 코드 생략하는 목적으로 설계
* 코드 가독성과 조합성을 높이는 것이 기본 목표
* 워크스테이션(연산자)
* 복원력
* 배압: 푸시 전용, 풀 전용, 풀-푸시
* 스케줄러

#### 프로젝트에 리액터 추가하기
```groovy
compile("io.projectreactor:reactor-core:3.2.0.RELEASE")
testCompile("io.projectreactor:reactor-test:3.2.0.RELEASE")
```

#### 리액티브 타입 - Flux와 Mono
* 리액티브 스트림 스펙: Publisher<T>, Subscriber<T>, Subscription, Processor<T, R>
* 리액터 프로젝트: Flux<T>, Mono<T>
* Flux, Mono는 서로 쉽게 변환이 가능

##### Flux
* 0, 1, 여러 요소 생성
* 무한대의 리액티브 스트림 생성 가능
* OutOfMemoryError 유발 가능하기 때문에 무한한 스트림 생성은 추천하지 않음

##### Mono
* 최대 하나의 요소만 생성
* CompletableFuture<T>와 의미론적으로 동일한 용도로 사용

> CompletableFuture vs Mono
> * 반드시 반환 값을 갖음 vs 반환 값이 없어도 됨
> * 즉시 처리 vs 구독자가 구독후 처리

##### RxJava 2의 리액티브 타입
* Flowable 타입만 리액티브 스트림과 호환
* 리액티브 타입을 의미적으로 세분화
* 리액티브 타입 - Observable
    * 배압을 지원하지 않음
    * null 값을 허용하지 않음
    * 리액티브 스트림 스펙과 비호환
    * Flowable보다 오버헤드가 적음
    * toFlowable
* 리액티브 타입 - Flowable
    * Flux와 동일한 역할
    * 리액티브 스트림 스펙과 호환
* 리액티브 타입 - Single
    * 하나의 요소를 생성
    * 리액티브 스트림 스펙과 비호환
    * 배압 전략을 필요로 하지 않음
    * toFlowable
* 리액티브 타입 - Maybe
    * Mono 타입과 동일한 의도
    * 리액티브 스트림 스펙과 비호환
    * toFlowable
* Completable
    * Mono 타입과 유사
    * onError, onComplete 신호만 발생
    * 리액티브 스트림 스펙과 비호환
    * toFlowable

#### Flux와 Mono 시퀀스 만들기
* 컬럭션, 숫자 등의 데이터를 기반으로 리액티브 스트림 생성
* Mono는 하나의 요소를 대상으로 하며 nullable, Optional과 함께 사용
* Mono는 HTTP 요청이나 DB 쿼리와 같은 비동기 작업 래핑에 유용
    * fromCallable(Callable)
    * fromRunnable(Runnable)
    * fromSupplier(Supplier)
    * fromFuture(CompletableFuture)
    * fromCompletionStage(CompletionStage)
* 빈 스트림과 오류만 포함하는 스트림
    * empty(): 빈 스트림
    * never(): 어떤 신호도 보내지 않는 스트림
    * error(Throwable): 오류를 전파하는 시퀀스
    * defer(): 순간에 행동을 경정하는 시퀀스
        → 사용자 로그인 세션 체크 
* 요소를 열거하여 시퀀스 생성
    * just()
    * justOrEmpty(): Optional을 Mono로 래핑
* 배열 또는 iterable 컬렉션을 통한 생성
    * fromArray()
    * fromIterable()

#### 리액티브 스트림 구독하기
* 모든 subscribe 메서드는 Disposable 인터페이스를 인스턴스를 반환
* subscribe()
    * 모든 신호 무시
* subscribe(Consumer<T> dataConsumer)
    * onNext
* subscribe(Consumer<T> dataConsumer, Consumer<Throwable> errorConsumer)
    * onNext, onError
* subscribe(Consumer<T> dataConsumer, Consumer<Throwable> errorConsumer, Runnable completeConsumer)
    * onNext, onError, onComplete
* subscribe(Consumer<T> dataConsumer, Consumer<Throwable> errorConsumer, Runnable completeConsumer, Consumer<Subscription> subscriptionConsumer);
    * 리액티브 스트림의 모든 요소 처리
    * 데이터 요청 처리를 통한 구독 제어
* subscribe(Subscriber<T> subscriber)
    * 구독자를 구현
    
##### 사용자 정의 Subscriber 구현하기
* 직접 Subscriber 인터페이스를 구현할 수 있음
* onSubscribe(), onNext(), onError(), onComplete() 구현
* 스스로 배압을 관리하며, TCK 요구사항을 올바르게 구현해야 한다는 어려움 존재  
    → BaseSubscriber 클래스를 상속  
    → hookOnSubscribe(), hookOnNext(), hookOnError(), hookOnCancel(), hookOnComplete() 재정의   
    → request(), requestUnbounded() 메서드 제공  

#### 연산자를 이용해 리액티브 시퀀스 변환하기
* 리액티브 시퀀스 원소 매핑하기
    * map(): 모든 원소를 새로운 값으로 매핑
    * index(): 시퀀스 원소를 열거 (Tuple2 클래스로 변환)
    * timestamp(): 타임스탬프 추가 (Tuple2 클래스로 변환)
* 리액티브 시퀀스 필터링하기
    * filter()
    * ignoreElements()
    * take()
    * takeLast()
    * takeUntil()
    * elementAt()
    * single()
    * skip()
    * take()
    * takeUntilOther()
    * skipUntilOther()
* 리액티브 시퀀스 수집하기
    * 리스트 타입으로 변환
        * Flux.collectList()
        * Flux.collectSortedList()
    * 맵 타입으로 변환
        * collectMap()
        * collectMultimap()
    * java.util.stream.Collector를 상속한 모든 형태의 데이터 구조로 변환 가능
        * collect(Collector)
    * repeat(): 입력 시퀀스 루프
    * defaultIfEmpty(): 빈 스트림에 대해 기본 값 반환
    * distinct(): 입력 스트림 중복 제거
    * distinctUntilChanged(): 무한 스트림에 중단 없는 행에 나타나는 중복 제거
* 스트림 원소 줄이기
    * count(): 원소수 카운트
    * all(): 모든 원소가 필요한 속성을 갖는지 확인
    * any(): 하나 이상의 원소가 필요한 속성을 갖는지 확인
    * hasElements(): 특정 원소가 하나라도 있는지 확인
    * hasElement(): 특정 원소가 있는지 확인
    * sort(): 백그라운드에서 원소 정렬
    * reduce(): 초깃값을 첫 번째 매개변수로 받고 이전 단계의 결과를 현재 단계의 원소와 결합하는 함수를 두 번째 매개벼수로 받음
    * scan(): 중간 결과를 다운스트림으로 보냄
    * then(), thenMany(), themEmpty(): 상위 스트림이 완료될 떄 동시에 완료되며 완료 또는 오류 신호만 보냄
* 리액티브 스트림 조합하기
    * concat(): 모든 원소를 연결 (업스트림이 순차적으로 처리됨)
    * merge(): 모든 원소를 연결 (하나의 다운스트림으로 병합, 업스트림은 동시에 처리됨)
    * zip(), zipWithIterable(): 업스트림이 하나의 원소를 보낼때까지 대기 후 원소를 결합
    * combineLatest(): 최소한 하나의 업스트림이 원소를 보내면 바로 새 값 생성
* 스트림 내의 원소 일괄 처리하기
    * buffer(): 여러개의 이벤트를 묶어서 이벤트 컬렉션을 전달
    * window(), windowUntil(): 원소를 분할하여 전파
    * groupBy(): 원소를 그룹화
    * groupJoin(): 특정 기간동안 처리되는 원소 그룹화
* flatMap, concatMap, flatMapSequential 연산자
    * flatMap(): map, flatten 2가지 작업으로 구성, 다른 스레드에서 처리
        * map: 원소를 변환
        * flatten: 새로운 리액티브 시퀀스로 병합
    * flatMapSequential()
    * concatMap()
    * flatMapDelayError(), flatMapSequenceDelayError(), concatMapDelayError(): 에러 시그널 지연
    * concatMapIterable(): 원소에 대한 Iterator를 생성
    
    | # |flatMap|flatMapSequential|concatMap|  
    |:----:|:----:|:----:|:----:|  
    | 연산자가 내부 스트림을 하나씩 구독하는지 여부| 하나씩 구독 | 하나씩 구독 | 하위 스트림을 생성하여 내부 스트림 처리가 완료되기를 기다림 |
    | 연산자가 생성된 원소와 순서를 유지하는지 여부| 유지 X | 큐를 사용하여 역순 유지 | 유지 |
    | 연산자가 다른 하위 스트림의 원소를 끼워 넣을 수 있는지 여부| 허용 | 허용하지 않음 | 허용하지 않음 |
* 샘플링하기
    * sample(), sampleTimeout(): 특정 기간 내 가장 최근에 본 값을 주기적으로 출력
* 리액티브 시퀀스를 블로킹 구조로 전환하기
    * toIterable(): Flux를 블로킹 Iterable로 변환
    * toStream(): Flux를 블로킹 스트림으로 변환
    * blockFirst(): 업스트림이 첫 번째 값을 보내거나 완료할 때까지 스레드 차단
    * blockLast(): 업스트림이 마지막 값을 보내거나 완료할 때까지 스레드 차단
* 시퀀스를 처리하는 동안 처리 내역 살펴보기
    * doOnNext(): 각 원소에 대한 어떤 액션을 수행
    * doOnComplete(), doOnError(): 완료, 에러 이벤트 발생시 호출
    * doOnSubscribe(), doOnRequest(), doOnCancel(): 구독 라이프 사이클 이벤트 발생시 호출
    * doOnTerminate(): 스트림 종료시 호출
    * doOnEach(): onSubscribe, onNext, onError, onComplete를 포함한 모든 신호처리
* 데이터와 시그널 변환하기
    * 데이터 스트림을 시그널 스트림으로 변환
    * materialize()
    * dematerialize()
    
#### 코드를 통해 스트림 만들기
* 스트림 내에서 시그널 생성
* 객체의 라이프 사이클을 리액티브 스트림의 라이프 사이클에 바인딩
* 팩토리 메서드
    * push()
        * 단일 스레드 생성자를 적용해 Flux 인스턴스를 프로그래밍 방식으로 생성
        * 기본 배압 및 취소 전략을 사용하여 비동기 API를 적용할 때 유용
    * create(): push()와 비슷하게 동작하지만 FluxSink 인스턴스를 직렬화하므로 다른 스레드에서 이벤트를 보낼 수 없음
    * generate(): 오브젝트 내부 전달 상태를 기반으로 복잡한 시퀀스를 만들 수 있도록 설계, 사용자 정의 리액티브 시퀀스를 생성  
        → 피보나치 수열(0, 1, 1, 2, 3, 5, 8, 13 ...)
* 일회성 리소스를 리액티브 스트림에 배치
    * using()
        * 일회성 리소스에 의존하는 스트림 생성
        * try-with-resources 방식의 접근법
        * Callable 인스턴스를 호출해 관리 자원을 동기적으로 검색
            ```java
            Flux<String> ioRequestResults = Flux.using(
                Connection::newConnection,
                connection -> Flux.fromIterable(connection.getData()),
                Connection::close
            );
            
            isRequestResults.subscribe(
                data -> log.info("Received data: {}", data),
                e -> log.info("Error: {}", e.getMessage()),
                () -> log.info("Stream finished")
            );
            ```  
        * usingWhen()
            * Publisher의 인스턴스에 가입해 관리되는 리소스를 리액티브 타입으로 검색
            * 단 하나의 연산자만으로 완전한 논블로킹 리액티브 트랜잭션을 구현
            ```java
            Flux.usingWhen(
                Transaction.beginTransaction(),
                transaction -> transaction.insertRows(Flux.just("A", "B", "C")),
                Transaction::commit,
                Transaction::rollback
            ).subscribe(
                d -> log.info("onNext: {}", d),
                e -> log.info("onError: {}", e.getMessage()),
                () -> log.info("onComplete")
            );
            ```
#### 에러 처리하기
* onError: 예외 시그널을 처리하며 핸들러를 정의하지 않을 경우 UnsupportedOperationException 발생
* onErrorReturn: 예외 발생시 예외 값 대체
* onErrorResume: 예외 발생시 대체 워크플로 실행
* onErrorMap: 예외 발생시 다른 예외로 변환
* retry: 예외 발생시 재시도 워크플로 정의
* retryBackoff: 예외 발생시 지수적인 백오프 알고리즘을 지원해 재시도할 때마다 대기 시간 증가
* defaultIfEmpty: 기본 값 변환
* switchIfEmpty: 다른 리액티브 스트림으로 변환
* timeout: 작업 대기 시간 제한하여 TimeoutException 발생
* delaySequence: 모든 시근널 지연

#### 배압 다루기
* 리액티브 스트림 스펙에서는 프로듀서와 컨슈머 간의 의사소통에 배압이 필요
* onBackPressureBuffer: 제한되지 않은 요구 요청, 다운스트림 컨슈머에 부하 발생히 큐를 이용해 버퍼
* onBackPressureDrop: 제한되지 않은 요구 요청, 다운스트림 처리 용량이 충분하지 않을 경우 데이터 삭제
* onBackPressureLast: 가장 최근에 수신된 원소를 기억하고 요청 발생시 다운스트림으로 푸시 (항시 최신데이터 유지)
* onBackPressureError: 제한되지 않은 요구 요청, 다운스트림 컨슈머가 처리를 유지할 수 없으면 오류 발생
* limitRate: 다운스트림 수요를 n보다 크지 않는 작은 규모로 나눔
* limitRequest: 다운스트림 컨슈머의 수요를 제한

#### Hot 스트림과 cold 스트림
* 콜드 퍼블리셔: 구독자가 나타났을 때 해당 구독자에 대해 모든 스퀀스 데이터가 생성되는 방식 (HTTP 요청)
    * range
    * defer: 핫 퍼블리셔를 콜드 퍼블리셔로 변환
* 핫 퍼블리셔: 구독자의 존재 여부에 의존하지 않고 데이터를 생성 (방송 시나리오)
    * just

##### 스트림 원소를 여러 곳으로 보내기
* ConnectableFlux: 데이터 생성 준비가 완료되는 대로 일부 구독자에게 공유
* publish, replay: 큐의 크기, 타임아웃 구성
* connectable, autoConnect, refCount: 원하는 임계값에 도달하면 실행을 기동할 다운스트림 구독자 수를 자동으로 추적
    
##### 스트림 내용 캐싱하기
* ConnectableFlux를 사용하면 다양한 데이터 캐싱 전략 구현 가능
* cache: 이벤트 캐싱

##### 스트림 내용 공유
* ConnectableFlux를 사용해 여러 개의 구독자에 대한 이벤트를 멀티캐스트
* share: 콜드 퍼블리셔를 핫 퍼블리셔로 변환

#### 시간 다루기
* interval: 주기적으로 이벤트 생성
* delayElements: 원소를 지연
* delaySequence: 모든 신호를 지연
* buffer, window: 데이터 버퍼링 분할
* timestamp, timeout: 시간 관련 이벤트 처리
* elapsed: 이전 이벤트와의 시간 간격 측정   
    -> 내부적으로 자바의 ScheduledExecutorService를 사용하기 때문에 정확한 지연을 보장하지 않음

#### 리액티브 스트림 조합하고 변환하기
* transform, composer: 스트림 라이프 사이클 결합 단계에서 스트림 동작을 한 번만 변경

#### Processor
* Publisher이면서 동시에 Subscriber이기 때문에 구독이 가능하고, 시그널(onNext, onError, onComplete)을 수동으로 보냄
* Processor보다 팩토리 메서드(push, create, generate)를 사용하는 것을 권장
* Direct 프로세서: 데이터 입력부를 사용자가 직접 구현해 데이터를 푸시만 할 수 있는 프로세스
    * DirectProcessor, UnicastProcessor
* Synchronous 프로세서: 업스트림 Publisher를 구독하거나 수동으로 데이터를 푸시만 할 수 있는 프로세스
    * EmitterProcessor, ReplayProcessor
* Asynchronous 프로세서: 여러 개의 업스트림 게시자에게 입력을 받아 다운스트림 데이터를 푸시할 수 있는 프로세스
    * WorkQueueProcessor, TopicProcessor
    * RingBuffer 데이터 구조 사용

#### 리액터 프로젝트 테스트 및 디버깅하기
* io.projectreactor:reactor-test
* 조립 단계에서 적용 가능한 디버깅 기능을 제공하며, 조립할 모든 스트림에 대해 스택 트레이스를 수집
```java
Hooks.onOperatorDebug()
```
* Flux, Mono 유형은 log 메서드를 통해 연산자를 통과하는 모든 신호를 기록할 수 있음

#### 리액터 추가 기능
* 리액터 애드온
    * reactor-adapter: RxJava2 리액티브 타입 및 스케줄러에 대한 어댑터 제공
    * reactor-logback: 고속의 비동기 로깅
    * reactor-extra: 고급 기능을 위한 추가 유틸리트 (TupleUtils, MathFlux ...)
* 리액터 RabbitMQ: RabbitMQ에 대한 리액티브 자바 클라이언트 제공
* 리액터 카프카:카프카 메시지 브로커와 유사한 기능 제공
* 리액터 네트: 리액티브 타입을 네티의 TCP/HTTP/UDP 클라이언트 및 서버에서 사용

### 리액터 프로젝트 심화학습
* 리액티브 스트림의 수명주기, 멀티스레딩, 리액터 프로젝트에서 내부 최적화 작동 방식

#### 리액티브 스트림의 수명 주기
##### 조립 단계
* 조립: 시행 흐름을 작성하는 프로세스
* 복잡한 처리 흐름을 구현할 수 있는 연쇄형 API 제공
* 빌더 패턴과 유사하게 보이지만 불변성을 제공하기위해 내부적으로 각각의 연산자가 새로운 객체를 생성
* 조립 단계에서 최적화 수행   
    -> concatWith: 동일한 연산자 체인일 경우 하나로 압축
```java
FluxFilter (
    MapFilter (
        FluxArray(1, 2, 3, 40, 500, 6000)
    )
)
```

##### 구독 단계
* 특정 Publisher를 구독할 때 발생
* 구독할 Publisher는 내부적으로 일련의 Publisher 체인으로 이루어짐
* 조립 단계와 래핑 구조가 역피라미드 형태를 띈다
* 구독 단계에서 최적화 수행
```java
ArraySubscriber (
    MapSubscriber (
        FilterSubscriber (
            Subscriber
        )   
    )
)
```

##### 런타임 단계
* 게시자와 구독자 간에 실제 신호가 교환
* onSubscribe 시그널과 request 시그널을 이용해 교환
* 런타임 단계에서 신호 교환량을 줄이기 위하여 최적화 수행
    * request 호출 횟수를 줄여 스트림 성능 향상
        * 내부적으로 volatile 필드를 대상으로 읽기/쓰기가 발생하여 고비용 발생
        * volatile: 주메모리 저장 표시    
    
* Subscription 래퍼 피라미드
```java
FilterSubscription (
    MapSubscription (
        ArraySubscription()
    )
)
```
* request 래퍼 피라미드 - 요청
* 런타임 중에 데이터는 소스로부터 Subscriber 체인을 거쳐 단계마다 다른 기능을 수행
```java
FilterSubscription(MapSubscription(ArraySubscription(...)))
    .request(10) {
        MapSubscription(ArraySubscription(...))
            .request(10) {
                ArraySubscription(...)
                    .requst(10) {
                        // 데이터 전송 시작
                    }
            }   
    }
```
* request 래퍼 피라미드 - 데이터 전달
```java
ArraySubscription.request(10) {
    MapSubscriber(FilterSubscriber(Subscriber)).onNext(1) {
        // 데이터 변환 로직을 작성합니다.
        FilterSubscriber(Subscriber).onNext("1") {
            // 필터 처리
            // 원소가 일치하지 않으면
            // 추가 데이터를 요청
            MapSubscription(ArraySubscription(...)),request(1) {...}
        }
    }
    
    MapSubscription(FilterSubscription(Subscriber)).onNext(20) {
        // 데이터 변환 로직을 작성합니다.
        FilterSubscriber(Subscriber).onNext("20") {
            // 필터 처리
            // 원소가 일치하면
            // 다운스트림 구독자에게 전송
            Subscriber.onNext("20") {...}
        }
    }
}
```

#### 리액터에서 스레드 스케줄링 모델
* 리액티브 멀티스레딩 실행을 위해 제공하는 연산자

##### publishOn 연산자
* 런타임 실행의 일부를 지정된 워커로 이동
* publishOn 연산자가 실행된 이후 Scheduler에 의해 별도의 스레드에서 실행
* 다운스트림에 대해서만 실행 영향 가능

https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#publishOn-reactor.core.scheduler.Scheduler-

> Scheduler: 런타임에 데이터를 처리할 워커를 지정하기 위한 개념으로 워커 또는 워커 풀을 나타내는 인터페이스
> Worker: Thread 또는 리소스를 추상화 한 것
> 직렬성: 원소는 하나씩 처리되므로 항상 모든 이벤트에 순서를 엄격하게 정의

##### publishOn 연산자를 이용한 병렬 처리
* publishOn 연산자는 처리 단계 사이에 비동기 영역을 추가하여 병렬 처리를 가능하게 함

##### subscribeOn 연산자
* 구독 체인에서 워커의 작업 위치를 변경
* 구독을 수행할 워커를 지정할 수 있음
* 구독 시간과 런타임 시간에 별도의 스레드 지정 가능
* 다운스트림/업스트림 실행 영향 가능

https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#subscribeOn-reactor.core.scheduler.Scheduler-

##### parallel 연산자
* 병렬처리를 위한 연산자로 하위 스트림에 대한 플로 분할과 분할된 플로 간 균형 조정 역할
* ParallelFlux라는 다른 유형으로 동작하며, Flux 간에 데이터의 크기가 균형을 이루도록 함
* 서로 다른 워커에서 처리 중인 데이터에 대한 작업 분배 가능
* runOn: publishOn 적용

##### Scheduler
* schedule, createWorker 라는 두 가지 핵심 메서드를 가진 인터페이스
* schedule: Runnable 작업을 예약하는 것이 가능
* createWorker: Runnable 작업을 예약할 수 있는 Worker 인터페이스의 인스턴스 제공
* SingleScheduler: 모든 작업을 한 개의 전용 워커에 예약
    * single    
* ParallelScheduler: 고정된 크기의 작업자 풀에서 작동 (CPU 코어 수로 제한, CPU 제한적 작업에 적합)
    * parallel
* ElasticScheduler: 동적으로 작업자를 만들고 스레드 풀을 캐시 (I/O 집약적 작업에 적합)
    * elastic
    
##### 리액터 컨텍스트
* Context는 리액터의 핵심요소로 런타임 단계에서 필요한 컨텍스트 정보에 엑세스할 수 있도록 한다
* Context는 멀티 스레드 엑세스 모델을 고려하여 Immutable 객체로 생성되어 새로운 요소를 추가하면 새로운 인스턴스로 변경 한다
* subscriberContext: 리액터의 Context에 접근

> ThreadLocal vs Context  
> * ThreadLocal은 단일 스레드를 이용할 때만 제대로 동작하기 때문에 비동기 처리 방식에서는 ThreadLocal을 상용할 수 있는 구간이 매우 짧음

#### 프로젝트 리액터의 내부 구조
* 연산자 융합

##### 매크로 퓨전
* 조립 단계에서 연산자를 다른 연산자로 교체하는 것에 목적으로 하며, 연산자들 간의 오버헤드를 줄이는 최적화 작업 수행 
    * Callable, ScalarCallable과 같은 인터페이스를 구현한 경우

```java
Flux.just(1)
    .publishOn(...) // 큐를 만들고, volatile 읽기 쓰기 발생
    // 큐를 만들 필요가 없는 subscribeOn 치환 가능
    .map(...)
```

##### 마이크로 퓨전
* 런타임 최적화 및 공유 리소스 재사용과 관련
* 조건부 연산자
    ```java
    Flux.from(factory)
        .filter(inspectionDepartment)
        .subscribe(store);
    ```
    
    * 필터 연사나자 같은 조건부 연산자가 전체 성능에 지대한 영향을 미침
        * 조건부 연산자를 통해 이동 중 일부 원소가 거부 될 경우 다운스트림 요구 사항을 충족하기 위해 request(1) 발생하는 경우
    * 마이크로 퓨전 유형의 ConditionalSubscriber 존재하여 이런 경우 최적화를 수행
        * 소스측에서 조건을 확인하여 추가적인 request(1) 없이 필요한 개수를 전송

* 연산자 간 비동기 경계
```java
Flux.just(1, 2, 3)
    .publishOn(Schedulers.parallel()) // 비동기 경계
    .concatMap(i -> Flux
                        .range(0, i) // 잠재적으로 업스트림으로 부터 i개의 원소 생성, 예측 불가능하여 큐가 필요
                        .publishOn(Schedulers.parallel())) // 비동기 경계
    .subscribe();
```

* 연산자 체인시 두 개의 비동기 경계가 포함
* 예측 불가능한 작업의 경우 배압을 처리하고 컨슈머 오버플로를 발생시키지 않으려면 처리 결과를 큐에 넣는 작업 필요 (concatMap)
* 경계 또는 경계 내부의 원소 체인에 공유 큐를 놓고 request 없이 업스트림 연산자가 큐를 사용하도록 최적화

> CAS(Compare and swap): 동시성 구현을 위해 값을 비교한 후 일치한 경우에 값을 교체하는 기법, 작업의 성공 여부에 따라 1 또는 0 값을 반환하는 단일 작업