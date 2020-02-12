### ApplicationContext extends ResourceLoader

- `ApplicationContext`가 상속받는 것중 `ResourceLoader`에 대해 살펴보자

- 리소스가 읽어오는 기능을 제공하는 인터페이스다.

- 리소스를 읽어오는 방법

  - 파일 시스템 읽어오기

  - classpath에서 읽어오기

    - target/classes가 root가 되고 이 밑으로 찾게 리소스를 찾게된다.

    ```java
    @Component
    public class AppRunner implements ApplicationRunner {
        @Autowired
        ResourceLoader resourceLoader;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            Resource resource = resourceLoader.getResource("classpath:test.txt");
            System.out.println(resource.exists());
            System.out.println(resource.getDescription());
        }
    }
    ```

    - 결과값을 확인해보면,
    - ![1577617383938](https://user-images.githubusercontent.com/40616436/71556445-154e8480-2a7c-11ea-9630-13df1ae29419.png)

  - URL로 읽어오기

  - 상대/절대 경로를 통해 읽어오기
