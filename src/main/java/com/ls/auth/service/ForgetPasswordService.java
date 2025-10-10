package com.ls.auth.service;

import com.ls.auth.model.request.PasswordResetRequest;
import com.ls.auth.model.request.PasswordUpdateRequest;
import com.ls.auth.model.entity.PasswordResetToken;
import com.ls.auth.repository.PasswordResetTokenRepository;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.repository.MemberRepository;
import com.ls.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgetPasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${email.password-reset-email-subject}")
    private String passwordResetEmailSubject;

    @Value("${email.password-reset-email-body}")
    private String passwordResetEmailBody;

    @Value("${email.password-reset-token-expiry}")
    private Long passwordResetTokenExpiry;


    public void requestPasswordReset(PasswordResetRequest request) {
        Optional<Member> member = memberRepository.findByEmail(request.getEmail());
        if (member.isEmpty()) {
            log.error("Email not found: {}", request.getEmail());
            throw new RuntimeException("Email not found");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(null,token, member.get(),LocalDateTime.now().plusMinutes(passwordResetTokenExpiry));
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:8082/api/password/reset?token="+token;
        String emailBody = MessageFormat.format(passwordResetEmailBody, resetLink);
        emailService.sendSimpleEmail(request.getEmail(), passwordResetEmailSubject, emailBody);
        log.info("Password reset email sent to: {}", request.getEmail());
    }

    public void resetPassword(PasswordUpdateRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        Member member = resetToken.getMember();
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);

        passwordResetTokenRepository.delete(resetToken);
        log.info("Password reset successfully for user: {}", member.getUsername());
    }
}
