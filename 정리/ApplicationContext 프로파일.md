### ApplicationContext extends EnvironmentCapable. 프로파일

- 프로파일 기능이란,

  - 운영을 유지보수하면서 추가적인 요구사항에서 대해 배포를 진행하게 된다면, 단계적인(local, test, stage) 서버 배포 후 최종적으로 테스트가 완료되면 운영(prd) 서버에 배포하게 된다.
  - 이 때, 각 서버마다 필요로 하는 Bean과 특정 환경에서만 Bean을 등록해야하는 경우, 또는 접근 URL을 다르게 하는 경우를 구별하기 위해 사용하는 것이 프로파일이다.

- 프로파일 정의해보자

  - 프로파일 정의하는 방법을 소스를 통해 살펴보자 (총 두가지 방법이 있다.)

    ```java
    //1. Configuration Class추가로 프로파일 정의
    @Configuration
    @Profile("test")
    public class TestConfiguration {
        @Bean
        public CustRepository custRepository() {
            return new TestCustRepository();
        }
    }
    
    //2. 해당 Class에 직접적으로 프로파일을 정의
    @Repository
    @Profile("test")
    public class TestCustRepository implements CustRepository{
    }
    
    //test환경이 아닌 곳에서 CustRepoistory를 꺼내오려고 하면 해당 Bean이 존재하지 않다라는 에러메시지를 보게 될것이다.  
    @Autowired
    CustRepository custRepository;
    
    ```

    - 1번 2번 모두 test환경에서만 CustRepository를 Bean으로 등록한 것이다.
    - @Profile은 `@Profile("!prod")` 방식도 사용가능하다. 즉, prod 환경이 아니면 모두 된다라는 뜻이다.

- 프로파일을 설정해보자

  - 프로파일을 설정은 Edit Configuration > VM Option > `-Dspring.profiles.active="test"`을 주면 된다.

- 다양한 방법으로 프로퍼티 설정 하자

  - VM Option을 사용하는 방법과 app.properties 파일을 이용해 프로퍼티를 설정하는 방법이 있다.

    - Edit Configuration > VM Option > `-Ddevice.name="android"`
    - app.properties

    ```html
    device.name = ios
    ```

    - 설정된 프로퍼티 확인

      ```java
      @Override
      public void run(ApplicationArguments args) throws Exception {
          Environment environment = ctx.getEnvironment();
          System.out.println(environment.getProperty("device.name"));
      }
      
      //결과값은 android가 나온다.
      //설정하는 것에서는 둘다 이상이 없지만 프로퍼티 내에서도 우선순위가 존재한다. 즉, VM Option을 통해 설정한 것이 더 높은 것을 확인할 수 있다.(JVM 시스템 프로퍼티)
      ```

- 프로퍼티 우선순위

  - ServletConfig 매개변수
  - ServletContext 매개변수
  - JNDI(java:comp/env/)
  - JVM 시스템 프로퍼티(VM Option)
  - JVM 시스템 환경 변수(운영체제 환경 변수)