### @Component와 컴포넌트 스캔

- 컴포넌트 스캔의 주요 기능

  - 스캔하고자 하는 위치를 설정할 수 있다. 즉, 어디서부터 어디까지 스캔할 것인지를 설정하는 것이다.
  - @filter를 사용하여 어떤 어노테이션을 스캔할 건지 아니면 안 할건지를 정할 수 있다.

- **@SpringBootApplication**

  - @SpringBootApplication이 위치한 패키지 안에 있는 것들만 스캔한다.
  - 즉, 해당 패키지 안에 있는 것들만 Bean으로 등록할 수가 있다.

- **@filter**

  - 현재 @SpringBootApplication은 TyeExcludeFilter.class와 AutoconfigurationExcludeFilter.class를 Bean등록에 제한하고 있다.

- 기본적으로 스캔되는 어노테이션

  - @Component에 해당하는 어노테이션을 Bean으로 등록한다.
    - @Repository
    - @Service
    - @Controller
    - @Configuration

- 패키지 밖에 있는 객체는 영원히 Bean으로 등록 못할까?

  - `ApplicationCotextInitalizer<GenericApplicationContext>`를 사용하면 패키지 밖에 있는 객체도 Bean으로 등록할 수 있다.

  - 해당 방법은 Functional한 Bean 등록 방식이다.

    ```java
    public static ovid main(String [] args) {
        
        @Autowired
        MyService myService;	//패키지 밖에 있는 객체
        
        var app = new Stringapplication(DemoApplication.class);
        app.addInitializers((ApplicationCotextInitalizer<GenericApplicationContext>) ctx -> {ctx.registerBean(MyService.class))};
        app.run(args);
    } 
    ```



---



- 컴포넌트 스캔의 동작 시점
  - 클라이언트가 정의한 Bean을 등록하기 전에  `BeanFactoryPostProcessor`를 구현한 `ConfigurationClassPostProcessor`에 의해 컴포넌트 스캔이 처리된다.
  - 즉, 다른 Bean(`@Bean` 혹은 `ctx.registerBean`을 통한 Bean)들을 등록하기 전에 컴포넌트 스캔을 통해서 Bean을 등록하는 것이다.



---



- 결과적으로 ComponentScan > @Bean > Funtional가 추천하는 방식이다.

