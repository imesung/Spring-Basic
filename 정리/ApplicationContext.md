### Application Context와 여러 Bean 설정 

- Application Context는 빈들을 설정하고 어디에 의존성을 주입할 건지를 정의하는 것이다.

- 빈을 설정하는 방법들을 살펴보자

- 첫번째로는 xml을 사용하는 방식이다.

  ```html
  <!-- application.xml -->
  <!-- xml 방식으로 빈 설정하기 -->
  <bean id = "custService"
        class ="mesung.springKeyTech.CustService">
  	<property name="custRepository" ref="custRepository"/>
  </bean>
  
  <bean id = "custRepository"
        class ="mesung.springKeyTech.CustRepository">
  </bean>
  ```

  - 그 후 main에서 ApplicationContext를 활용해서 빈을 가져와 사용하면 된다. (의존성 주입)

    ```java
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
     	String [] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        
        //Bean 단일적으로 가져오기
        CustService custService = (CustService) context.getBean("custService");
    }
    ```

  - 그치만 해당 방식은 모든 빈들을 일일히 등록해야 된다는 단점이 발생한다.

  - 그래서 나타난 것이 `component-scan`이 생겨났다.

  ```html
  <context:component-scan base-package="mesung.springKeyTech"/>
  <!-- mesung.springKeyTech 패키지부터 스캔을 하여 빈으로 등록하겠다라는 의미 -->
  ```

  - 스캐닝을 할 때는 기본적으로 `@Component` 어노테이션을 사용해서 빈 등록을 한다. 
  - @Component를 확장한 어노테이션을 사용해서 빈 등록 가능
    - @Service
    - @Repository
    - @Controller
  - 그럼 의존성 주입을 받기 위해서는? (빈을 사용하는 것)
    - @Autowired
    - @Inject
      - Inject는 또 다른 의존성을 필요로 한다.
    - 주로 @Autowired를 사용한다.

- 두번째로는 Java를 활용해서 빈을 등록한다. 일명 Java 설정 파일

  ```java
  @Configuration
  public class ApplicatoinConfig {
      @Bean
      public CustRepository custRepository() {
          return new CustRepository();
      }
      
      //setter를 활용한 의존성 주입
      @Bean
      public CustService custService() {
          CustService custService = new CustService();
          //custService에 setter가 있으므로 직접 의존성 주입을 받을 수 있다.
          custService.setCustRepository(custRepository());
          return CustService;
      }
      
      //메서드 파라미터를 활용한 의존성 주입
      @Bean
      public CustService custService(CustRepository custRepository) {
          CustService custService = new CustService();
          custService.setCustRepository(custRepository;
          return CustService;
      }
                                        
  	//의존성 주입을 직접하지 않고 @Autowired를 사용해서 의존성 주입을 한다.
  	@Bean
      public CustService custService() {
  		return new CustService();
          
          //CustService 내에서
          //@Autowired
          //private CustRepository custrepository; 및 setter
          //선언 되어 있어야 함.
          //즉, @Bean을 통해 Bean으로 등록되어 있어 꺼내와서 사용 가능
      }                            
  }
                                        
  //ApplicationContext 활용
  public static void main(String[] args) {
      //해당 클래스(ApplicationConfig)를 빈 설정 파일로 사용한다.
      ApplicationContext context = new AnnotationConfigApplicationContext(ApplicatoinConfig.class);
      
   	String [] beanDefinitionNames = context.getBeanDefinitionNames();
      System.out.println(Arrays.toString(beanDefinitionNames)); 
      //Bean 단일적으로 가져오기
      CustService custService = (CustService) context.getBean("custService");
  }
  ```

  - 얘 또한, @Bean을 일일히 등록한다는 단점이 발생함.
  - java의 componentScan을 사용하자

  ```java
  @Configuration
  @ComponentScan(basePackageClasses = DemoApplication.class)
  //DemoApplication은 main 객체
  //해당 ComponentScan은 type safe한 방식으로 현 스프링 부트기반 가장 가까운 방법
  public class ApplicationConfig {
  
  }
  ```

- 세번째로 SpringBoot를 활용하여 빈 설정파일들을 제거하자.

  ```java
  //DemoApplication.java
  @SpringBootApplication
  //@SpringBootApplication을 사용하면 ApplicationConfig.java 또한 필요 없다. 즉, @SpringBootApplication 하나로 빈 설정이 가능하다.
  public class DemoApplication {
      public static void main(String[] arges){
          
      }
  }
  ```

  - SpringBoot에서는 @SpringBootApplication 것만 정의 되어 있으면 빈 설정 파일이 필요없다.
  - @Configuration와 @ComponentScan는 SpringBoot가 아닌 일반적인 Spring에서 빈 설정을 하는 방법이다.
