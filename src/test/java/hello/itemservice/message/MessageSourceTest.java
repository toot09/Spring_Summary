package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

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

}
