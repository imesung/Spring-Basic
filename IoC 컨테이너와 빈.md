### 스프링 IoC 컨테이너와 빈

- IoC 컨테이너란

  - BeanFactory
  - Bean 설정 및 정의를 읽어들이고, Bean을 구성하고 제공하는 것이다.

- Bean이란

  - IoC 컨테이가 관리하는 객체를 말한다.
  - 객체를 Bean으로 설정하는 방법도 여러가지가 있다.

- 스프링 IoC 컨테이너에 등록되어 있는 Bean들은 Scope 자체가 싱글톤으로 구성되어 있다.

- 라이플사이클 인터페이스 제공

  - Bean이 만들어지는 시점에서 클라이언트가 임의적으로 하고 싶은 행동을 주입할 수 있다.

  - Ex. @PostConstruct를 사용

    ```java
    @PostConstruct
    public void postContstruct() {
        System.out.Println("-----------");
        System.out.Println("내가 하고 싶은 행위");
    }
    ```

    



### 의존성 주입

- 어떤 객체가 사용하는 의존 객체를 직접 생성하는게 아니라, 스프링에서 주입 받아 사용하는 것을 말하고, 이 때 주입해주는 것이 IoC 컨테이너에서 정보를 가져오는 것이다.

- A객체가 B객체를 의존하고 있고, IoC 컨테이너에 의해 의존성 주입을 받는 다면, A객체는 단일 테스트 하기가 어렵다

  - Why? 테스트를 진행할 때는 IoC 컨테이너를 통해 의존 객체를 받아올 수 없기 때문이다.

  - 그러면 IoC 컨테이너를 통한 의존성 주입을 받지 않으면 되지 않을까?

    - 이 방법은 더욱 어려워 진다. 소스를 통해 살펴보자

    ```java
    //CustService 객체
    public class CustService {
        //1. CustSerVice 내부에서 CustRepository 직접 의존
        private CustRepository custRepository = new CustRepository();
        //CustService객체안에 custRepository 객체를 직접 생성하므로 순수하게 CustService만을 테스트할 수 없다.
        
        //2. 의존성 주입을 통해 의존
        private CustRepository custRepository;
        public CustService(CustRepository custRepository) {
            this.custRepository = custRepository;
        }
        //테스트에서는 IoC 컨테이너에서 의존성 주입을 받지 못하므로 일반적인 방법으로는 테스트하기가 어렵다. -> 테스트에서 가짜 객체를 만들면 된다!(@Mock)
        
        public cust(Cust cust) {
            return custRepository.save(cust);
        }
    }
    
    //CustRepoistory 객체
    public class CustRepoistory {
        public Cust save(Cust cust) {
            return null;
        }
    }
    
    
    //Test 2번을 해결해보자
    public class CustServiceTest {
        
        //가짜 객체를 만든다.
        @Mock
       	CustRepository custRepository;
        
        @Test
        public void save() {
            Cust cust = new Cust();
            //save메서드가 실행될 때 cust가 들어오면 cust를 리턴하라는 내용
            when(custRepository.save(cust)).thenReturn(cust);       
        }
    }
    ```

    

- 즉, 테스트에서 의존성 주입 역할을 사용하려면 @Mock을 통해 가짜 객체를 만든 후 사용하면 된다..