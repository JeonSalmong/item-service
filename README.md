# item-service
springboot sample project (spring version 3.0.12)

### 스프링 기본
#### Filter
* log 필터 적용
* login 필터 적용 -> interceptor로 변경 적용
#### Interceptor 적용
* login 적용 -> Spring Security로 변경 적용
#### AOP 적용
* start/end 타임 출력 로그 적용 됨
#### Converter 적용
* integer to string
* ipport to string
* string to integer
* string to ipport
#### Formatter 적용
* number 적용
#### Session Manager 적용
* LoginContrllor에서 Login체크시 사용 -> Spring Security로 변경 적용되면서 미사용
#### Exception
* Exception resolver 적용
#### ArgumentResolver
* Login 적용 -> Spring Security로 변경 적용되면서 미사용
#### Validator 적용
* ItemValidator 적용
#### 메시지, 국제화적용
* error, message properties 적용
#### File
* 단일, 멀티 파일 업/다운로드 적용
#### JPA 적용
* 현재 H2 DB 연결
* AuditorAware 적용
#### Spring Security 적용
* UserDetail 적용(Session)
* OAuth 추가 적용 예정
* API Controller 대상 JWT 적용 예정
### 스프링 활용
#### AutoConfiguration
* memory.jar libs 추가
#### 외부설정과 프로필 적용
* local, dev, prod yml 분리
* -Dspring.profiles.active 설정에 따른 pay 로직(임시) 분기 처리
#### 액츄에이터 적용
* 기본path actuator -> manage로 변경 (Security에서 Admin만 접속 되도록 설정)
#### 모니터링(프로메테우스, 그라파나)
* 1차 : springboot -> 프로메테우스(DB) -> 그라파나 적용
* 2차 : springboot (push)-> 프로메테우스 push gateway <-(pull) 프로메테우스 -> 그라파나
* push gateway, 프로메테우수, 그라파나는 docker desktop으로 실행 (C:\Study\springMVC\docker)
* Timer, Counter, Gauge 적용
* HttpCounterConfig 구성(app 서버 ip, port 값 전달)
* pushgateway 서버 연결이 불가능 한 경우에 대한 예외처리는???
### 스프링 고급
#### 커스텀 LogTrace 적용
* LogTrace의 TraceId 동시성 이슈를 쓰레드로컬로 해결
* Order orderList()에 적용
#### 템플릿메서드패턴 적용
* 커스텀 LogTrace 적용의 단점 보완
  * 코드 중복성
  * 비지니스로직/공통로직 혼재
  * 공통로직 수정시 모두 찾아서 수정해 줘야 함
* Order add()에 적용
#### 전략패턴 (Context - Strategy)
* 템플릿메서드패턴의 단점 보완
  * 상속(자식클래스가 부모클래스의 기능을 전혀 사용하지 않지만 강하게 의존)
* 전략패턴 (Context - Strategy)
  * Context : 변하지 않는 템플릿 역할
  * Strategy : 변하는 알고리즘 역할(비지니스 로직)
  * Context에 Strategy 구현제를 주입하는 형태 (스프링의 의존관계 주입이 바로 전략 패턴)
#### 템플릿콜백패턴 (Template - Callback) 적용
* 전략패턴의 단점 보완
  * 전략패턴은 선조립(선주입)후실행 방식
  * 조립이후 전략 변경이 번거롭다
* 전략을 파라미터로 전달 받는 방식
  * Context -> Template
  * Strategy -> Callback
  * JdbcTemplate, RedisTemplate 등 XxxxTemplate 클래스가 대부분 이 패턴임
* Order processCancelBuy()에 적용