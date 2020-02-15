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