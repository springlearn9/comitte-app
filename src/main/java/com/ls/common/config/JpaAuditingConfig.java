package com.ls.common.config;

import com.ls.auth.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuration for JPA auditing.
 * Provides automatic population of createdBy and updatedBy fields with the current user's ID.
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Slf4j
public class JpaAuditingConfig {

    /**
     * Provides the current auditor (logged-in user's ID) from the security context.
     * Uses @Lazy to avoid circular dependency with JPA repositories.
     * 
     * @return AuditorAware bean that returns the current user's member ID
     */
    @Bean
    public AuditorAware<Long> auditorProvider(@Lazy MemberRepository memberRepository) {
        return new AuditorAwareImpl(memberRepository);
    }

    /**
     * Implementation of AuditorAware that extracts the current user's ID.
     */
    @Slf4j
    static class AuditorAwareImpl implements AuditorAware<Long> {
        
        private final MemberRepository memberRepository;

        public AuditorAwareImpl(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }

        @Override
        public Optional<Long> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() 
                    || "anonymousUser".equals(authentication.getPrincipal())) {
                log.warn("No authenticated user found for auditing - authentication: {}", 
                        authentication != null ? authentication.getPrincipal() : "null");
                return Optional.empty();
            }

            try {
                // Principal is now LoginUserDetails (set in JwtAuthenticationFilter)
                Object principal = authentication.getPrincipal();
                
                if (principal instanceof com.ls.auth.model.response.LoginUserDetails) {
                    com.ls.auth.model.response.LoginUserDetails userDetails = 
                        (com.ls.auth.model.response.LoginUserDetails) principal;
                    Long memberId = userDetails.getMemberId();
                    log.debug("Current auditor: {} (ID: {})", userDetails.getUsername(), memberId);
                    return Optional.of(memberId);
                } else if (principal instanceof String) {
                    // Fallback for String principal (shouldn't happen with current setup)
                    String username = (String) principal;
                    log.info("Getting auditor for username: {}", username);
                    return memberRepository.findByUsername(username)
                            .map(member -> {
                                log.info("Current auditor set: {} (ID: {})", username, member.getMemberId());
                                return member.getMemberId();
                            });
                } else {
                    log.error("Unexpected principal type: {}", principal.getClass().getName());
                    return Optional.empty();
                }
            } catch (Exception e) {
                log.error("Error retrieving current auditor: {}", e.getMessage(), e);
                return Optional.empty();
            }
        }
    }
}
