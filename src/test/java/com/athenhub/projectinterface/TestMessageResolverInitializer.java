package com.athenhub.projectinterface;

import com.athenhub.commoncore.message.MessageResolver;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 테스트 환경에서 {@link MessageResolver}를 초기화하기 위한 유틸리티 클래스.
 *
 * <p>이 클래스는 메시지 번들을 로드하여 {@link MessageResolver}의 {@code messageSource} 필드에 주입합니다. 주로 단위 테스트에서
 * 국제화(i18n) 메시지 조회를 가능하게 하기 위해 사용됩니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * @BeforeEach
 * void setUp() {
 *     TestMessageResolverInitializer.initializeFromResourceBundle();
 * }
 * }</pre>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public class TestMessageResolverInitializer {

  /**
   * 테스트용 메시지 번들을 로드하고 {@link MessageResolver}에 주입한다.
   *
   * <p>messages.properties 파일을 classpath에서 읽고 UTF-8 인코딩을 적용합니다.
   */
  public static void initializeFromResourceBundle() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();

    // messages.properties 파일 로드
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");

    ReflectionTestUtils.setField(MessageResolver.class, "messageSource", messageSource);
  }
}
