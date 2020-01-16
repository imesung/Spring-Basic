## Resource 추상화

**java.net.URL을 추상화 한 것이다.**



### 추상화한 이유

- java.net.URL은 classpath 기준으로 리소스를 읽어오는 기능이 부재되어 있다.
- ServletContext를 기준으로 상대 경로를 읽어오는 기능이 부재되어 있다.
- URL은 기본적으로 프로토콜 http, https, ftp, file, jar를 지원해준다.
- **이런 부분들을 통합해서 하나의 추상화로 만든것이다.**



### 구현체

- ClassPathResource : classpath 접두어를 사용하면 해당 classpath 기준으로 리소스를 찾는다.
- FileSystemResource : filesystem 기준으로 리소스를 찾는다.
- ServletContextResource : 사실상 가장 많이 사용. 웹 애플리케이션 루트에서 상대 경로로 리소스를 찾는것이다.



### 리소스 읽어오기

- Resource 타입은 location 문자열과 **ApplicationContext 타입**에 따라 결정된다.

```java
Object ctx = new FileSystemXmlApplicationContext("xxx.xml");	//파일 시스템 리소스를 읽어온다.
Object ctx = new ClassPathXmlApplicationContext("xxx.xml");	//classpath 리소스를 읽어온다.
//WebApplicationContext 밑으로부터는 모두 ServletContextResource를 읽어오는 것이다.
```

- FileSystem 혹은 Classpath등 ApplicationContext를 제외하고 **WebApplicationContext 이하**는 모두 **ServletContextResource**를 읽어온다.
  - WebApplicationContext
- ApplicationContext 타입에 상관 없이 리소스 타입을 강제하려면 java.net.URL 접두어(classpath://) 중 하나를 사용할 수 있다.
  - **classpath**:me/whiteship/config.xml -> ClassPathResource
  - **file**://some/resource/path/config.xml -> FileSytemResource
  - **접두어를 사용하여 리소스를 설정하는 것이 명시적으로 표현되어 있어 추천한다**



