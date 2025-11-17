package com.athenhub.projectinterface.global.infrastructure.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Spring Data JPA의 Auditing 기능을 활성화하기 위한 설정 클래스.
 *
 * <p>{@link EnableJpaAuditing}을 통해 엔티티의 생성자와 수정자 정보를 자동으로 기록할 수 있으며, {@code auditorAwareRef}를 통해
 * 감사자 정보를 제공할 {@link AuditorAwareImpl} 빈을 지정한다.
 *
 * <p>이 설정이 활성화되면 엔티티의 {@code @CreatedBy}, {@code @LastModifiedBy}, {@code @CreatedDate},
 * {@code @LastModifiedDate} 필드가 자동으로 채워진다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@Configuration
public class JpaAuditingConfig {}
