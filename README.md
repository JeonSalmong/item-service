# item-service
springboot sample project (spring version 3.0.12)

### 주요 적용된 기능 포인트
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
#### AutoConfiguration
* memory.jar libs 추가
#### 외부설정과 프로필 적용
* local, dev, prod yml 분리
* -Dspring.profiles.active 설정에 따른 pay 로직(임시) 분기 처리
#### 액츄에이터 적용
* 기본path actuator -> manage로 변경 (Security에서 Admin만 접속 되도록 설정)
#### 모니터링
* 1차 : springboot -> 프로메테우스(DB) -> 그라파나 적용
* 2차 : springboot (push)-> 프로메테우스 push gateway <-(pull) 프로메테우스 -> 그라파나
* push gateway, 프로메테우수, 그라파나는 docker desktop으로 실행 (C:\Study\springMVC\docker)
* Timer, Counter, Gauge 적용
* HttpCounterConfig 구성(app 서버 ip, port 값 전달)
* pushgateway 서버 연결이 불가능 한 경우에 대한 예외처리는???
#### 커스텀 LogTrace 적용
* LogTrace의 TraceId 동시성 이슈를 쓰레드로컬로 해결
