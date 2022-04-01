Spring의 Message 국제화 복기

### 1. application.properties 메세지 추가
spring.messages.basename=messages

### 2. massages properties 추가
예)
기본(한국어) : massages.properties
```
hello=안녕
hello.name =안녕 {0}
```
영어 : massages_en.properties
```
hello=hello
hello.name =hello {0}
```

>추가로 나의 경우 한글로 테스트할때 ??로 떴다(IntelliJ)
아래 인코딩 설정 후 정상으로 처리
참고 : https://hanke-r.tistory.com/155

### 3. Bean 등록
```
@Bean
public MessageSource messageSource() {
      ResourceBundleMessageSource messageSource = new
  ResourceBundleMessageSource();
      messageSource.setBasenames("messages", "errors");
      messageSource.setDefaultEncoding("utf-8");
      return messageSource;
}
```
MessageSource로 Bean이 등록된다. Springboot에서는 따로 등록을 안해줘도 지원해준다.

### 4. Test
```
	@Autowired
    MessageSource ms;

    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        Assertions.assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode() {
        // 코드가 없을때 Exception처리
        assertThatThrownBy(()-> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMesssage() {
        // 기본메시지
        String result =  ms.getMessage("no_code", null, "기본 메시지", null);
        Assertions.assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage() {
        // Argument는 Object[]로 세팅
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        Assertions.assertThat(result).isEqualTo("안녕 Spring");
    }

    @Test
    void defaultLang() {
        // Default
        Assertions.assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        Assertions.assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang() {
        // 영어
        Assertions.assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
```
