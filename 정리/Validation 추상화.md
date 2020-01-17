## Validation 추상화

**애플리케이션에서 사용하는 객체들을 검증하는 인터페이스다.**

**Bean Validation 1.0과 1.1을 지원한다.**






### Validator 인터페이스 사용하기

**두 가지 메소드를 구현해야한다.**

- boolean supports(class clazz) : 어떤 타입의 객체를 검증할 때 사용할 것인지 결정한다.
- void validate(Object obj, Error e) : 실제 검증 로직을 이 안에서 구현한다.
  - 구현할 때 ValidationUtils를 사용하면 편리하다.



**소스를 통해 validator를 만들어 보자**

```java
//Event
public class Event {
    Integer id;
    String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

//EventValidator
public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", "메시지를 찾지 못했을 때의 값");
    }
}

//AppRunner
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Event event = new Event();
        EventValidator eventValidator = new EventValidator();
        Errors errors = new BeanPropertyBindingResult(event, "event");

        eventValidator.validate(event, errors);

        System.out.println(errors.hasErrors());

        errors.getAllErrors().forEach(e -> {
            System.out.println("=========error code==========");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });

    }
}
```

1. Event는 현재 title을 주지 않았다.
2. **eventValidator.validate(event, errors);** 실행 시
3. EventValidator의 validate() 메소드에서는 "title"이 **rejectIfEmpty**이기 때문에 에러가 발생할 것이다.
4. **System.out.println(errors.hasErrors());** => true가 나올 것이다.
5. **errors.getAllErrors()**로 모든 에러들을 갖고 오고 에러들을 출력해주는 것이다.
6. ![image](https://user-images.githubusercontent.com/40616436/72532243-65b84580-38b6-11ea-93bc-b998f63f2953.png)



### 최근 Validation 방식

**위의 방식은 원초적인 방식이다. 요즘들어 사용하는 Validation 방식을 살펴보자**

Spring을 사용하게 되면 Spring이 자동으로 **LocalValidatorFactoryBean**(Bean Validation)을 등록해준다.

그로인해 Validator 클래스를 구현하지 않고 validation을 할 수 있다.



**소스로 확인해보자**

```java
//Event
public class Event {
    Integer id;

    @NotEmpty
    String title;

    @NotNull @Min(0)
    Integer limit;

    @Email
    String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

//AppRunner
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println(validator.getClass());

        Event event = new Event();
        event.setLimit(-1);
        event.setEmail("a123");

        Errors errors = new BeanPropertyBindingResult(event, "event");

        validator.validate(event, errors);

        System.out.println(errors.hasErrors());

        errors.getAllErrors().forEach(e -> {
            System.out.println("=========error code==========");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });

    }
}
```

1. **@Autowired Validator**를 통해 **LocalValidatorFactoryBean**을 불러와 주입한다.
2. Event에 변수를 추가한 후 어노테이션을 통해 해당 필드값이 어떤 형식인 지 설정한다.
   1. **@NotEmpty** : 빈값이면 안된다.
   2. **@NotNull @Min(0)** : Null이면 안되고 최소값은 0이어야 한다.
   3. **@Email** : Email 형식으로 되어야 한다.
   4. @Size : 컬렉션의 사이즈를 최소 혹은 최대값을 설정해줄 수 있다.
3. event.set(***)를 통해서 에러를 유도해본다.
4. **결과값을 살펴보자**
5. ![image](https://user-images.githubusercontent.com/40616436/72533461-8ed9d580-38b8-11ea-99f9-a30e036c9b24.png)
   1. 첫번째 줄에서 현재 validator가 LocalValidatorFactoryBean이라는 것을 확인할 수 있다.
   2. 각 필드가 설정한 범위 혹은 조건에 만족하지 않을 때의 에러 메시지를 출력해준다.
      1. 오류내용, 클래스명, 필드명
      2.  오류내용, 필드명
      3.  오류내용, 필드 타입
      4.  오류내용
      5.  오류 상세 내용
**즉, LocalValidatorFactoryBean으로 통해 우리는 validator를 구현하지 않아도 간편히 validation 체크를 할 수 있는 것이다!**

