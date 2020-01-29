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

