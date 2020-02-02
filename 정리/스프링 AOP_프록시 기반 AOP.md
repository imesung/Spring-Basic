## 스프링 AOP: 프록시 기반 AOP

**스프링 AOP 특징**

- 프록시 기반의 AOP 구현체이다.
- 스프링 Bean에만 AOP를 적용할 수 있다.
- 모든 AOP의 기능을 제공하는 것이 목적이 아니라, 스프링 IoC와 *엔터프라이즈 애플리케이션에서 가장 흔한 문제*에 대한 해결책을 제공하는 것이 목적이다.



**프록시 패턴**

![image](https://user-images.githubusercontent.com/40616436/73373374-de2df600-42fb-11ea-9c36-d24a75507220.png)

- Real Subject : 원래 해야할 일
- Proxy : 원래 해야할 일을 참조
- 즉, Subject의 타입은 Proxy를 사용하고 Proxy는 Real Subject를 감싸서 실제 클라이언트의 요청을 처리하게 된다.
  - 이렇게 하는 이유는? **접근 제어 혹은 유연한 부가 기능 추가** 때문이다.



**예제를 통해 살펴보자**

EventService : **Subject**

SimpleEventService : **Real Subject**

~~~java
//Subject
public interface EventService {

    void createEvent();
    void publishEvent();
}

//Real Subject
@Service
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");
    }

    @Override
    public void publishEvent() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");
    }
}
~~~

단순히 EventService의 구현체로 SimpleEventService를 구현한 것이다.



**이제 Proxy 패턴을 사용해보자**

현재 시스템에서 Client(AppRunner.java) 코드와 Real Subject의 코드를 수정하지 않고 기능(Real Subject의 메소드를 실행하는 시간을 측정하는 기능)을 추가해보자

~~~java
//Real Subject
@Service
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");

        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");

        System.out.println(System.currentTimeMillis() - begin);
    }
  
  	@Override
    public void deleteEvent() {
        System.out.println("Delete an event");
    }
}
~~~

단순히 이런 식으로 각 메소드의 실행 시간을 측정할 수 있는데, 이런 코드는 각 메소드마다 중복적인 소스가 나타나므로 이런 현상을 **Crosscutting Conserns** 이다.

즉 메소드를 추가할 때마다 실행 시간을 측정하는 명령어가 모두 들어가게 되는 것이다. 이런 부분을 proxy 패턴을 활용해서 해결해보자.



**Proxy 패턴 및 AOP**

ProxySImpleEventService에서 메소드에 직접적으로 접근하지 않고 부가적인 기능을 추가하고 있다.

~~~java
@Primary
@Service
public class ProxySimpleEventService implements EventService{

    //보통 인터페이스의 Bean을 주입받아 사용해야 하지만, proxy의 경우는 Real Subject의 Bean을 주입받아 사용해야 한다.
    @Autowired
    SimpleEventService simpleEventService;

    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.createEvent();
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.publishEvent();
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void deleteEvent() {
        simpleEventService.deleteEvent();
    }
}
~~~

하지만 이 부분에도 중복되는 코드는 존재한다. 또한, SimpleEventService(Real Subject)뿐만 아니라 다른 클래스에도 proxy가 필요하다면 모두 일일이 추가해야하는 경우가 발생한다.



**proxy 단점 해결 -> Spring AOP**

단점을 해결하기 위해 우리는 두가지 관점을 활용한 Spring AOP를 사용할 수 있다.

- 동적으로 proxy 객체를 만들자 -> 런타임 시에 동적으로 어떤 객체에 proxy 객체를 만들자.
- Spring IOC를 활용하자



**Spring AOP**

Spring AOP 구성

- SimpleEventService(Real Subject)가 Bean으로 등록된다
- BeanPostProcessor(Bean을 가공할 수 있는 LifeCycle 인터페이스)를 구현한 **AbstractAutoProxyCreator로 Real Subject Bean을 감싸는 Proxy Bean을 만든다.**
- 해당 Proxy Bean을 Real Subject Bean 대신에 등록을 해준다.



**참고**

토비의 스프링 3 

 