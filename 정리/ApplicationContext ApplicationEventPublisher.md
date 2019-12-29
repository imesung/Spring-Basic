### ApplicationContext extends ApplicationEventPublisher

- `ApplicationContext`가 상속받는 것중 `ApplicationEventPublisher`에 대해 살펴보자

- Event 만들어보기

  - 스프링 4.2 전에는 `ApplicationEvent`를 상속 받은 후 Event를 만들어야 했으나, 그 이후 부터는 해당 클래스를 상속받지 않아도 Event로 사용할 수 있다.

    ```java
    public class MyEvent  {
    
        private int data;
    
        private Object source;
    
        public MyEvent(Object source, int data) {
            this.source = source;
            this.data = data;
        }
    
        public Object getSource() {
            return source;
        }
    
        public int getData() {
            return data;
        }
    }
    ```

  - `MyEvent`를 보면 스프링 소스 코드가 나의 코드에 노출되지 않는 것이 핵심이다.

    - 이것이 POJO 기반의 프로그래밍이고 이로 인해 테스트하기 편하고 유지보수 또한 원활하게 할 수 있다는 장점이 있다.

- Event 발생 시키기

  - `ApplicationContext`가 상속받는`ApplicationEventPublisher`를 사용하여 Event를 발생시키면 된다.

    ```java
    //Runner
    @Component
    public class AppRunner implements ApplicationRunner {
    
        @Autowired
        ApplicationEventPublisher publishEvent;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            publishEvent.publishEvent(new MyEvent(this, 500));
        }
    }
    ```

- Event 처리하기(Handler)

  - 스프링 4.2전에는 ApplicationListener<MyEvent>를 구현한 클래스를 만들어 Bean으로 등록하여 사용해야 했으나, 그 이후 버전부터는 `@EventListener`를 사용해서 Bean의 메소드에 사용할 수가 있다.

    ```java
    @Component
    public class MyEventHandler {
        @EventListener
        public void handler(MyEvent myEvent) {
            System.out.println("MyEvent를 받았고, 데이터는 " + myEvent.getData());
        }
    }
    ```

- Event 핸들러가 여러개 있을 경우

  - 같은 Event를 바라보는 handler가 여러개 있을 경우에 동일하게 Event를 사용한다.

    ```java
    //MyEventHandler2 추가
    @Component
    public class MyEventHandler2 {
        @EventListener
        public void handler(MyEvent myEvent) {
            System.out.println(Thread.currentThread().toString());
            System.out.println("handler2에서 MyEvent를 받았고, 데이터는 " + myEvent.getData());
        }
    }
    ```

  - 하나의 thread에서 동일한 Event를 사용하는 것을 볼수 있다.

  - ![1577615883301](https://user-images.githubusercontent.com/40616436/71556387-614cf980-2a7b-11ea-8843-b0f449e6c0c7.png)

  - 만약 Event를 사용하는 핸들러의 순서가 중요하다면 순서를 정할 수가 있다.

    - `@Order`를 사용해서 각 핸들러에 우선순위를 두면 된다.

      ```java
      //handler1
      @EventListener
      @Order(Ordered.HIGHEST_PRECEDENCE)
      public void handler(MyEvent myEvent) {
          System.out.println(Thread.currentThread().toString());
          System.out.println("handler1에서 My Event를 받았고, 데이터는 " + myEvent.getData());
      }
      
      //handler2
      @EventListener
      @Order(Ordered.HIGHEST_PRECEDENCE + 2)
      public void handler(MyEvent myEvent) {
          System.out.println(Thread.currentThread().toString());
          System.out.println("handler2에서 MyEvent를 받았고, 데이터는 " + myEvent.getData());
      }
      ```

    - ![1577616263112](https://user-images.githubusercontent.com/40616436/71556390-64e08080-2a7b-11ea-888d-202a49cd4943.png)

  - 비동기적으로 실행하고 싶다면 `@Async`를 붙여서 사용하면 된다.

    - 이때는, main Application 파일에 `@EnableAsync`를 붙이고, 핸들러의 전에 작성한 `@Order`자리 대신에 `@Async`를 붙여주면 된다.



---



- 스프링이 제공하는 기본 이벤트
  - `ContextRefreshedEvent`: `ApplicationContext`를 초기화 했거나 refresh 했을 때 발생한다.
  - `ContextStartedEvent`: `ApplicationContext`를 start()하여 lifecycle bean들이 시작 신호를 받는 시점에 발생한다.
  - `ContextStoppedEvent`: `ApplicationContext`를 stop()하여 lifecycle bean들이 정지 신호를 받는 시점에 발생한다.
  - `ContextClosedEvent`: `ApplicationContext`를 close()하여 싱글톤 bean이 소멸되는 시점에 발생한다.
  - `RequestHandledEvent`: HTTP 요청을 처리했을 때 발생한다.
