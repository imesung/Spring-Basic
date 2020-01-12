### ApplicationContext extends MessageSource

- ApplicationContext가 상속받는 것중 MessageSource에 대해서 살펴보자

- MessageSource는 메시지 설정 파일을 모아놓고 각 국가에 맞는 메시지를 제공할 수 있다.

- Spring Boot에서는 `ResourceBundleMessageSource`가 Bean으로 등록되어 있어 `.properties`만 설정되어 있으면 메시지를 가져와 사용할 수 있다.

  - `messages.properties`
  - `message_ko_KR.properties`
  - `message_en_US.properties`
  - 등등..

  ```java
  //messages.properties
  greeting=Hello, my name is {0}
  
  //message_ko_KR.properties
  greeting=안녕하세요, 제 이름은 {0}
  
  //Runner
  MessageSource.getMessage("greeting", new String[]{"richard"}, Locale.KOREA);
  ```

  - `MessageSource.getMessage`는 여러 파라미터를 받는데,
    - `MessageSource.getMessage(String code, Object[] args, String default, Locale loc);`
      - 가장 기본적인 것으로, 특정 로케일 내에서 발견되는 메시지가 없으면 default message가 반환된다.
    - `MessageSource.getMessage(String code, Object[] args, Locale loc);`
      - 해당 메소드는 default message가 없어 특정 로케일 내에서 발견되는 메시지가 없으면 `NoSuchMessageException`이 발생한다.

  

---



- 좀 더 재미있는 접근을 해보자 MessageSource는 릴로딩 기능이 있다는 것이다.

  ```java
  //main Application
  @Bean
  public MessageSource messageSource() {
      ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();	//리로드 가능(운영중 메시지 변경 가능)
      messageSource.setBasename("classpath:/messages");
      messageSource.setCacheSeconds(3);	//캐싱 시간은 최대 3초
      return messageSource;
  }
  
  //Runner
  @Override
  public void run(ApplicationArguments args) throws Exception {
      while(true) {
          System.out.println(messageSource.getMessage("greeting", new String[]{"mesung"}, Locale.KOREA));
          Thread.sleep(1000l);
      }
  }
  
  //messages_ko_KR.properties
  greeting=hello, {0}
  ```

  - 1초 간격으로 메시지를 출력해내겠다는 소스이고 결과값은 아래와 같다
    - ![1577612541784](https://user-images.githubusercontent.com/40616436/71556403-aa04b280-2a7b-11ea-997c-8b2f641de69c.png)
  - 그런데 여기서 서버를 중지시키지 않고 messages_ko_KR.properties를 `gretting=hello world, {0}`로 변경을 하게 되면 결과값은 신기하게도 우리가 변경했던 소스를 바로 적용하는 모습을 볼 수 있다.
    - ![1577612637453](https://user-images.githubusercontent.com/40616436/71556405-abce7600-2a7b-11ea-839a-a0b8c4ac66d7.png)
