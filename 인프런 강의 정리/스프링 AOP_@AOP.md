## 스프링 AOP: @AOP

**Spring AOP 구성**

- SimpleEventService(Real Subject)가 Bean으로 등록된다
- BeanPostProcessor(Bean을 가공할 수 있는 LifeCycle 인터페이스)를 구현한 **AbstractAutoProxyCreator로 Real Subject Bean을 감싸는 Proxy Bean을 만든다.**
- 해당 Proxy Bean을 Real Subject Bean 대신에 등록을 해준다.

**즉, Spring Boot로 인해 Proxy Bean을 동적으로 만들어주고, 구현한 Aspect가 Proxy 역할을 하는 것이다.**

- Aspect내에서는 해야할 일(Advice)들을 개발자 재량에 따라 구현하면 되는 것이다.



## 

그럼 이제 Spring Boot에서 제공하는 Proxy 기반의 Spring AOP를 적용해보자.



## 

**Spring AOP Dependency 추가**

~~~java
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
  </dependency>
  
~~~



## 

**Aspect 추가**

![image](https://user-images.githubusercontent.com/40616436/73606364-cc469e80-45ec-11ea-9505-9510edc916d1.png)

Aspect는 그림과 같이 해야할 일(Advice, 띠)와 어디에 적용할 것인지(PointCut, 각 객체 혹은 객체의 메소드(A,B,C))가 필요하다.



## 

**Aspect 적용을 소스로 살펴보자**

~~~java
@Component
@Aspect
public class PerfAspect {

    //Advice가 적용되는 대상(createEvent, publishEvent 메소드 자체라고 생각하면 됨)
    //즉, 메소드를 감싸고 있다고 생각하면 됨.
    //타겟의 메소드를 호출하고 결과값을 리턴
    @Around("execution(* com.mesung..*.EventService.*(..))") //EventService 안에 있는 모든 메소드에 logPerf() 기능을 적용해라
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed();  //해당 메소드
        System.out.println(System.currentTimeMillis() - begin);
        return retVal;
    }

}
~~~

- @Aspect : 해당 클래스를 Aspect로 사용하겠다.
- @Around : 대상 객체의 메소드의 실행 전, 후 또는 에러 발생 처리가 가능하다.
  - execution : 현재 소스에서는 com.mesung 패키지 밑에 Eventservice 객체 안에 있는 메소드들을 모두 적용하겠다라는 의미이다.
  - @Around("bean(simpleEventService)") : simpleEventService bean에 있는 메소드들을 모두 적용하겠다.

**실행결과**

![image](https://user-images.githubusercontent.com/40616436/73606576-0dd84900-45ef-11ea-8bc8-2550caab8633.png)

그런데 우리는 모든 메소드를 호출하는 것이 아니라 deleteEvent는 제외시키고 싶은데, deleteEvent가 나타나는 것을 확인할 수 있다.



## 

**이 때는 애노테이션을 만들어서 활용하자**

~~~java
//perfLogging 애노테이션

/*
* 메소드 성능 시간을 측정한다.
* */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PerfLogging {
}

//SimpleEventService(Real Subject)
@PerfLogging
@Override
public void createEvent() {
  try {
    Thread.sleep(1000L);
  } catch (InterruptedException e) {
    e.printStackTrace();
  }
  System.out.println("Created an event");
}

@PerfLogging
@Override
public void publishEvent() {
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    e.printStackTrace();
  }
  System.out.println("Published an event");

}

@Override
public void deleteEvent() {
  System.out.println("Delete an event");
}

//PerfAspect
@Component
@Aspect
public class PerfAspect {
    @Around("@annotation(PerfLogging)")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed();  //해당 메소드
        System.out.println(System.currentTimeMillis() - begin);
        return retVal;
    }
}
~~~

기동되는 순서는 다음과 같다.

1. AppRunner를 통해 SimpleEventService 각 메소드들을 실행하는데 각 메소드에 붙여진 애노테이션을 확인한다.

2. @PerfLogging 애노테이션의 정보를 확인한다.

3. 정보 확인 후 Spring AOP이 적용된 것을 런타임 시점에 확인 후 해당 @Asepct의 정보를 확인한다.

4. 확인 결과, logPerf()는 **Advice인 @Around**를 통해 @PerfLogging이 붙여진 클래스 혹은 메소드에 해당 로직을 적용한다.

5. @PerfLogging이 붙여진 SimpleEventService의 createEvent()와 PublishEvent()만 성능 시간을 나타낸 것을 확인할 수 있다.

6. 실행결과

   ![image](https://user-images.githubusercontent.com/40616436/73606944-627dc300-45f3-11ea-96c7-7675934a56fd.png)

   

## 

**각 애노테이션들을 살펴보자**

~~~java
@Retention(RetentionPolicy.CLASS)
public @interface PerfLogging {
}
~~~

여기서 살펴보면 @Retention이라는 애노테이션을 사용하는데, @Retention은 해당 애노테이션 정보를 얼마나 유지시킬 것인지를 나타내는 것이다.

- RetentionPolicy.CLASS는 .class(클래스 파일)까지 유지하겠다라는 것이고, 이것이 기본값이다.
  - 해당 객체는 바이트코드까지 남아 있는다.
  - @Retention을 사용할 때는 클래스 파일 이상 유지시켜야 한다.
- RetentionPolicy.SOURCE
  - 컴파일하고 사라진다.
- RetentionPolicy.RUNTIME
  - 런타임까지 살아있는 것인데, 굳이 여기까지 사용하지는 않아도 된다.
- @Documented와 @Target을 사용하는 좋다.

~~~java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PerfLoggin {
  
}

//@Documented : Java Doc 만들 때 Documentation이 될 수 있도록 한다.
//@Target(ElementType.METHOD) : AOP 형태로 만들어 줄 것이 메소드 형태다라는 것을 알려준다.
~~~



## 

**또 다른 Advice**

- @Before : 어떤 메소드가 실행되기 이전에 작업을 하겠다.

  ~~~java
  @Before("bean(simpleEventService)")
  public void hello() {
    System.out.println("hello");
  }
  ~~~

  ![image](https://user-images.githubusercontent.com/40616436/73607024-63fbbb00-45f4-11ea-95ef-7de31c17b88c.png)

- @AfterReturning

- @AfterThrowing

- @Around