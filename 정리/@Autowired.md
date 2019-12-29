### @Autowired

- 필요한 의존 객체의 "타입"에 해당하는 Bean을 IoC 컨테이너에서 찾아 주입하는 것이다.

- @Autowired를 사용할 수 있는 위치는 총 세가지 있다

- 생성자, setter, field

  - 생성자에 붙여서 사용하는 것 부터 살펴보자

  ```java
  //CustService 객체
  @Service
  public class CustService {
  	CustRepository custRepository;
      
      @Autowired
      public CustService(CustRepository custRepository) {
          this.custRepository = custRepository
      }
  }
  
  //CustRepoistory 객체
  @Repository
  //@Repository를 붙이는 이유는 @Repository가 붙여있는 Bean들만 컨트롤이 가능하고 AOP에서 사용하기가 더 좋기 때문이다.
  public class CustRepoistory {
     
  }
  ```

  - setter로 붙이는 것을 확인해보자

  ```java
  //CustService 객체
  @Service
  public class CustService {
  	CustRepository custRepository;
      
      @Autowired
      public setCustReposstory(CustRepository custRepository) {
          this.custRepository = custRepository
      }
  }
  
  //CustRepoistory 객체
  @Repository
  public class CustRepoistory {
     
  }
  ```

  - field에 붙여보자

  ```java
  //CustService 객체
  @Service
  public class CustService {
      @Autowired
      CustRepository custRepository;
  }
  
  //CustRepoistory 객체
  @Repository
  public class CustRepoistory {
     
  }
  ```



---



- 근데 만약, 의존 받으려는 해당 타입의 빈이 없거나, 한개, 여러개일 경우에는 어떤 식으로 해결할까?

- 해당 타입의 빈이 없는 경우

  - 즉,  CustRepository에 @Repository가 안 붙여있는 경우를 말하고, 결과적으로 에러가 발생한다.

    - 생성자에서는 @Repository가 붙여져 있지 않으면 CustService라는인스턴스를 생성하다가 그 인스턴스에 필요한 CustRepository Bean을 찾을 수 없으므로 에러가 발생하여 직관적으로 오류 해결을 할 수 있다. **즉, CustRepository에 @Repository 붙이면 해결!**

    - 그러나 setter를 통해 의존성 주입을 할 때는 단순히 setter를 통해 가져오는 거니깐 CustService라는 인스턴스를 만들 수 있지 않을까? 답부터 말하자면 **맞다, 만들 수 있다**

      - 하지만, CustService에는 @Autowired가 있기 때문에 해당(setter) 의존성을 주입하려고 시도를 한다. 그로 인해 에러가 발생하는 것이다.
      - 그렇다면 해결 방법은 없을 까?

      ```java
      //@Autowired는 기본값이 true이기 때문에 기본적으로 의존하려는 Bean을 찾지 못하면 애플리케이션 구동에 실패하게 된다. -> 기본값을 false로 바꿔주자
      
      @Autowired(required = false)
      public setCustReposstory(CustRepository custRepository) {
          this.custRepository = custRepository
      }
      ```

      - 이 방식은 CustRepository의 의존성을 주입하지 않은 CustService 인스턴스를 생성한 것 뿐이다.
      - field 또한, setter와 마찬가지로 동일한 오류 내용이고, `@Autowired(required = false)`를 사용하여 해당 인스턴스만 생성할 수 있다.

  - 생성자의 경우 `@Autowired(required = false)`를 사용할 수 없지만 setter나 field는 `@Autowired(required = false)`를 사용하여 의존객체를 주입받지 않더라고 생성까지는 가능하다.

- 해당 타입의 빈이 여러개인 경우

  ```java
  //interface
  public interface CustRepository {
      
  }
  
  @Repository
  public class MyCustRepository implements CustRepository {
      
  }
  
  @Repository
  public class MesungCustRepository implements CustRepository {
      
  }
  
  //CustService
  public class CustService {
      @Autowired
      CustRepository custRepository;
  }
  ```

  - 위 같이 같은 타입의 객체를 두개 이상 정의하고 의존성 주입을 받을 시 주입을 받지 못한다.

  - Spring은 어떤 의존성을 주입해야하는 지 모르는 것이다.

  - 이 때의 해결방법은? @Primary, @Qualifier, 해당 타입의 빈 모두 받기

    - **@Primary**

      ```java
      @Repository
      public class MyCustRepository implements CustRepository {
          
      }
      
      @Repository @Primary
      public class MesungCustRepository implements CustRepository {
          
      }
      ```

      - @Primary를 사용할 시 같은 타입의 객체가 여러개 있을 경우 해당 어노테이션이 붙은 객체를 의존받겠다라는 의미가 된다.

    - **@Qualifier**

      ```java
      //CustService
      public class CustService {
          @Autowired @Qualifier("mesungCustRepository")
          CustRepository custRepository;
      }
      ```

      - @Qualifier를 사용할 시 mesungCustRepository를 의존 받을 수 있는 것이다.

    - **해당 타입의 빈 모두 받기**

      ```java
      //CustService
      public class CustService {
          @Autowired
          List<CustRepository> custRepositories;
          
          public void printCustRepository() {
              this.custRepositories.forEach(System.out::println);
          }
      }
      ```

    - 추가적으로 알고만 있어도 되는 부분은 Repository의 이름을 통해 해당 타입의 빈을 의존 받을 수도 있다.

      ```java
      //CustService
      public class CustService {
          @Autowired
          CustRepository myCustRepository;	//이름으로 의존받으려는 객체를 정함
      }
      ```

      - 하지만 이 방식은 그리 추천하는 방식이 아니다.

  - 가장 추천하는 방식은 @Primary 방식이니 참고만 하면 될 것 같다.

    - 이유 중 하나는 Type Safe 하니깐!



---



- 동작 원리
  - @Autowired는 BeanPostProccessor라는 인터페이스의 구현체인 AutowiredAnnotationBeanPostProcessor에 의해 의존성을 주입해주는 것이다.
  - 주입하는 시점은 postProcessBeforInitialization에서 주입을 진행해준다. 이 시점은 애플리케이션 구동 중에 실행된다.