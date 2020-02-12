## Bean 등록 및 의존관계 설정

### Bean 등록에 필요한 어노테이션

- **@Component** : <bean>태그와 동일한 역할
- **@Repository** : 퍼시스턴스 레이어, 데이터베이스 관련 클래스
- **@Service** : 비즈니스 로직을 가진 클래스
- **@Controller** : 웹 요청과 응답을 처리하는 클래스



### Bean 의존관계 주입 어노테이션

- **@Autowired**

  - Spring DI 컨테이너에서 주입하고자 하는 Bean을 불러온다.

  - Setter 메소드, 필드, 생성자에 적용할 수 있다.

  - Bean 검색 할 때 우선순위는 1. byType, 2. byName 이다.

  - 주입 받아올 Bean을 명시할 수 있다.

    ~~~java
    @Autowired
    @Qualifier("CustService")	//Type 다음 Name(변수명)
    ~~~

    

- **@Resource**

  - Spring이 아닌 Java에서 객체에 주입하고자 하는 객체의 레퍼런스를 불러온다.

  - Setter 메소드에 적용할 수 있다.

  - Bean 검색 할 때 우선순위는 1. byName, 2. byType 이다.

  - 주입 받아올 객체를 명시할 수 있다.

    ~~~java
    @Resource(name="custService")	//Name(변수명) 다음 Type
    ~~~

    

