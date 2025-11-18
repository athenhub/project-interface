package com.athenhub.projectinterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * 테스트 환경에서 Spring Security의 인증 정보를 설정하기 위한 커스텀 애노테이션.
 *
 * <p>Spring Security의 {@link org.springframework.security.test.context.support.WithSecurityContext}
 * 메커니즘을 활용하여, 테스트 실행 시 지정된 사용자 정보로 SecurityContext 를 구성한다.
 *
 * <p>컨트롤러, 서비스, 리포지토리 테스트 등에서 인증이 필요한 로직을 검증할 때 편리하게 사용할 수 있으며, 사용자 UUID, username, 표시 이름(name),
 * Slack ID, 역할(roles) 등을 자유롭게 설정할 수 있다.
 *
 * <p>사용 예:
 *
 * <pre>
 * {@code
 *   @Test
 *   @MockUser(username = "master", roles = {"MASTER"})
 *   void testMasterAccess() {
 *       // 인증된 MASTER 사용자 컨텍스트에서 테스트 실행
 *   }
 * }
 *
 * @see WithMockUserSecurityContextFactory
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface MockUser {

  /** 테스트 사용자 UUID. 기본값은 모든 값이 0으로 채워진 UUID. */
  String uuid() default "00000000-0000-0000-0000-000000000000";

  /** 테스트 사용자 로그인 ID(username). Spring Security의 principal name으로 사용된다. */
  String username() default "testUser";

  /** 테스트 사용자의 표시 이름. */
  String name() default "테스트사용자";

  /** 테스트 사용자의 Slack ID. */
  String slackId() default "testSlackId";

  /** 테스트 사용자의 역할 목록. "USER", "ADMIN" 등 문자열로 지정하면 자동으로 "ROLE_" prefix가 추가된다. */
  String[] roles() default "USER";
}
