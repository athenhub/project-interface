package com.athenhub.projectinterface.global.infrastructure.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * JPA Auditing 기능에서 사용할 감사자(Auditor) 정보를 제공하는 구현체.
 *
 * <p>Spring Security의 인증 컨텍스트에서 JWT 기반 인증 정보를 조회하여 `preferred_username` 클레임 값을 감사자로 설정합니다. 인증 정보가
 * 없거나 JWT가 존재하지 않을 경우 기본 감사자 이름으로 {@code "SYSTEM"}을 사용합니다.
 *
 * <p>이 구현체는 엔티티 생성·수정 시 자동으로 기록되는 {@code createdBy}, {@code lastModifiedBy} 필드의 값을 결정하는 데 사용됩니다.
 *
 * <pre>
 * 사용 예:
 * - 엔티티 저장 시 createdBy = authenticatedUser
 * - 엔티티 수정 시 lastModifiedBy = authenticatedUser
 * </pre>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

  /**
   * 현재 인증된 사용자명을 반환한다.
   *
   * <p>SecurityContext에 저장된 인증 정보에서 JWT를 추출하여 {@code preferred_username} 클레임을 조회한다. 인증 정보가 없을 경우
   * 기본값으로 {@code "SYSTEM"}을 사용한다.
   *
   * @return 현재 감사자명(Optional)
   */
  @Override
  public Optional<String> getCurrentAuditor() {
    String username = "SYSTEM";
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
      username = jwt.getClaim("preferred_username");
    }
    return Optional.ofNullable(username);
  }
}
