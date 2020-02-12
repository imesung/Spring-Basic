## IoC와 DI

### IoC의 개념

- **Inversion of Control의 약자로서, 객체의 생성과 생명주기에 대한 권한이 역전되는 것을 말한다.**



### IoC 컨테이너

- **객체의 생성과 생명주기를 관리하고, POJO의 생성과 초기화, 서비스, 소멸에 대한 권하을 가진다.**



### DI의 개념

- **Dependency Injection의 약자로서, Bean 설정 정보를 바탕으로 객체간의 의존 관계를 연결해주는 것을 말한다.**



**빈 설정 방법**

- **xml로 설정**

  ~~~java
  //1. 객체마다 Bean 설정
  <bean id = "custService" class ="mesung.springKeyTech.CustService">
      <property name="custRepository" ref="custRepository"/>
  </bean>
  
  <bean id = "custRepository" class ="mesung.springKeyTech.CustRepository"></bean>
    
  //2. 패키지로 Bean 설저
  <context:component-scan base-package="mesung.springKeyTech"/>
  ~~~

  1. CustService와 CustRepository를 Bean으로 등록한다.

  1. CustService와 CustRepoistory는 서로 의존 관계를 갖는다.
  2. mesung.springKeyTech 하위에 있는 모든 객체를 Bean으로 등록한다.



- **Java로 설정**

  ~~~java
  //1. 객체마다 Bean 설정
  @Configuration	//Java Bean 설정 어노테이션
  public class ApplicationConfig {
    /*
    CustRepository를 Bean으로 설정 
    CustService와 CustRepository 의존 관계 설정
    */
    
    @Bean	//CustRepository Bean 설정
    public CustRepository custRepository() {
      return new CustRepository();
    }
  
    //setter를 활용한 의존성 주입
    @Bean
    public CustService custService() {
      CustService custService = new CustService();
      custService.setCustRepository(custRepository());
      return CustService;
    }
  
    //메서드 파라미터를 활용한 의존성 주입
    @Bean
    public CustService custService(CustRepository custRepository) {
      CustService custService = new CustService();
      custService.setCustRepository(custRepository);
      return CustService;
    }
  }
  
  //2. DemoApplication이 속한 패키지 하위에 모든 Bean 설정
  @Configuration
  @ComponentScan(basePackageClasses = DemoApplication.class)
  public class ApplicationConfig {}
  ~~~

  1. 각 메소드의 반환값을 통해 Bean을 등록하며, 등록된 Bean(CustRepository)를 불러와 CustService와 의존관계를 맺는다.
  2. DemoApplication.java가 속한 패키지의 모든 객체는 Bean으로 등록된다.



- **SpringBoot로 설정**

  ~~~java
  //DemoApplication.java
  @SpringBootApplication
  public class DemoApplication {
  }
  ~~~

  @SpringBootApplication 하나로 DemoApplication이 속한 패키지 하위의 모든 객체는 Bean으로 등록된다.



### Spring DI 컨테이너의 개념

BeanFactory로서, Bean(POJO 객체)들을 관리한다.

**BeanFactory** : Bean을 생성, 조회, 반환에 대해서 관리하고, getBean()이라는 메소드가 정의되어 있다.

**ApplicationContext** : BeanFactory에 스프링에 관련된 여러 부가 서비스 기능을 추가한 것이고, BeanFactory를 구현하는 구현체이다.

![image](https://user-images.githubusercontent.com/40616436/74351615-b316cc80-4dfa-11ea-8730-25cca7d94ef3.png)

