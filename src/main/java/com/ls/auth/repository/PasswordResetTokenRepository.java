package com.ls.auth.repository;

import com.ls.auth.model.entity.PasswordResetToken;
import com.ls.comitte.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByMember(Member member);
}
