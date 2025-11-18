package com.athenhub.projectinterface.auth.infrastructure;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스.
 *
 * <p>이 설정은 JWT 기반 인증 구조를 위한 Stateless 환경을 구성하고, 사용자 정의 인증 필터인 {@link LoginFilter}를 Spring Security
 * 필터 체인에 등록합니다. CSRF 설정은 REST API 특성에 맞춰 비활성화되며, 모든 요청에 대해 기본적으로 접근을 허용합니다.
 *
 * <p>또한 인증 실패(401) 및 권한 부족(403) 상황에 대해 공통적으로 HTTP 401 상태 코드를 반환하도록 예외 처리 핸들러를 구성합니다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginFilter loginFilter;

  /**
   * Spring Security 필터 체인을 구성한다.
   *
   * <p>주요 설정:
   *
   * <ul>
   *   <li>CSRF 비활성화
   *   <li>{@link LoginFilter}를 {@link UsernamePasswordAuthenticationFilter} 앞에 등록
   *   <li>세션을 사용하지 않는 Stateless 정책 적용
   *   <li>모든 요청 허용 (추후 필요 시 구체적 인가 규칙 추가 가능)
   *   <li>인증 실패 및 인가 실패 시 401 Unauthorized 반환
   * </ul>
   *
   * @param http Spring Security {@link HttpSecurity} 객체
   * @return 구성된 {@link SecurityFilterChain}
   * @throws Exception 보안 구성 중 오류 발생 시
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(CsrfConfigurer::disable)
        .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeHttpRequests())
        .exceptionHandling(
            c -> {
              c.authenticationEntryPoint(authenticationEntryPoint());
              c.accessDeniedHandler(accessDeniedHandler());
            });

    return http.build();
  }

  private static Customizer<
          AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
      authorizeHttpRequests() {
    return authorize -> {
      authorize.anyRequest().permitAll();
    };
  }

  private static AuthenticationEntryPoint authenticationEntryPoint() {
    return (req, res, e) -> {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    };
  }

  private static AccessDeniedHandler accessDeniedHandler() {
    return (req, res, e) -> {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    };
  }
}
