### Bean의 스코프

- Bean의 스코프

  - 싱글톤

    - 싱글톤이란 같은 타입의 객체를 여러개 생성해도 같은 동일한 객체가 생성되는 것이다. 즉, 하나의 객체만이 생성되는 것을 말한다.
    - 기본적으로 Bean은 싱글톤 Scope으로 되어있다.

  - 프로토타입

    - 프로토타입이란 같은 타입의 객체를 여러개 생성할 시 각각 새로운 객체가 생성되는 것이다.
    - 프로토타입 Scope로 지정하고 싶다면 해당 객체에 `@Scope("prototype")`을 지정해주면 된다.
    - Request
    - Session
    - WebSocket

  -   싱글톤과 프로토타입을 섞어서 쓴다면 문제가 발생하지 않을까?

    - 프로토타입 객체에서 싱글톤 타입을 사용한다면은 문제가 발생하지 않을 것이다.

      ```java
      @Component @Scope("prototype")
      public class Prototype {
          @AUtowired
          Singleton singleton;
      }
      ```

      - Class Prototype은 객체를 생성할 때마다 새로운 객체를 만들어내고 Class Singleton은 동일하게 나타날 것이다.

    - 하지만, 싱글톤 Scope인 객체에 프로토타입 Scope인 객체를 의존하게 된다면 이슈가 발생하게 된다.

      ```java
      //prototype
      @Component @Scope("prototype")
      public class Prototype {
      }
      
      //singleton
      @Component
      public class Singleton {
          @Autowired
          private Prototype prototype;
      
          public Prototype getPrototype() {
              return prototype;
          }
      }
      ```

      - 싱글톤 Scope 객체에 프로토타입 Scope 객체를 의존할 시, 의존하는 객체가 프로토타입이더라도 동일한 객체를 생성하는 이슈가 발생한다.

      - 그렇다면 해결방법은 없는가? 프록시를 활용해라

        ```java
        @Component @Scope(value="prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public class Prototype {
        }
        ```

        - Class Prototype을 Proxy로 감싸 Class Singleton이 직접적으로 참조하지 못하게 막는 것이다.
        - why? 직접적으로 참조하게 된다면 Class Prototype이 바뀔 여지가 없기 때문이다. Class Prototype은 객체가 생성될 때마다 매번 바뀌어야 하므로!!
        - ![1577595911138](https://user-images.githubusercontent.com/40616436/71556277-b5ef7500-2a79-11ea-8957-5e081fcaee67.png)

  - 싱글톤 객체 사용 시 주의할 점

    - 싱글톤 객체는 프로퍼티가 공유되므로 thread safe하지 않을 수 있으니 주의해야한다.
    - 모든 싱글톤 Scope의 Bean들은 기본적으로 ApplicationContext 초기 구동을 할 때 인스턴스를 생성하므로 구동 시에 시간이 걸릴 수도 있다.

