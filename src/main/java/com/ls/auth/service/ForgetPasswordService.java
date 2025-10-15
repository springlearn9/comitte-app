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

import java.security.SecureRandom;
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

    private SecureRandom secureRandom = new SecureRandom();

    public void requestPasswordReset(PasswordResetRequest request) {
        String userNameOrEmail = request.getUsernameOrEmail();
        Optional<Member> member = getMemberByUsernameOrEmail(userNameOrEmail);
        PasswordResetToken resetToken = preparePasswordResetToken(member.get());
        passwordResetTokenRepository.save(resetToken);
        sendPasswordResetEmail(userNameOrEmail, resetToken);
        log.info("Password reset email sent to: {}", request.getUsernameOrEmail());
    }

    public void resetPassword(PasswordUpdateRequest request) {
        Member member = validateTokenAndGetMember(request);
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
        log.info("Password reset successfull for user: {}", member.getUsername());
    }

   private Member validateTokenAndGetMember(PasswordUpdateRequest request) {
       Member member = null;
        if(request.getToken()!=null) {
            Optional<PasswordResetToken> resetTokenOptional = passwordResetTokenRepository.findByToken(request.getToken());
            if(resetTokenOptional.isPresent()) {
                passwordResetTokenRepository.delete(resetTokenOptional.get());
                member = resetTokenOptional.get().getMember();
            }
            else {
                log.error("token invalid or not exists");
                throw new RuntimeException("token invalid or not exists");
            }
        } else {
            member = getMemberByUsernameOrEmail(request.getUsernameOrEmail()).get();
            Optional<PasswordResetToken> resetTokenOptional = passwordResetTokenRepository.findByMember(member);
            if(request.getOtp().equals(resetTokenOptional.get().getOtp())) {
                passwordResetTokenRepository.delete(resetTokenOptional.get());
                log.info("otp validation successfull for user: {}", member.getEmail());
            }
            else {
                log.error("otp validation failed for user: {}", member.getEmail());
                throw new RuntimeException("otp validation failed");
            }
        }
        return member;
    }


    private void sendPasswordResetEmail(String userNameOrEmail, PasswordResetToken resetToken) {
        String resetLink = "http://localhost:8082/api/password/reset?token="+ resetToken.getToken();
        String emailBody = MessageFormat.format(passwordResetEmailBody, resetLink, resetToken.getOtp());
        emailService.sendSimpleEmail(userNameOrEmail, passwordResetEmailSubject, emailBody);
    }

    private Optional<Member> getMemberByUsernameOrEmail(String userNameOrEmail) {
        Optional<Member> member = memberRepository.findByUsername(userNameOrEmail);
        if (member.isEmpty()) {
            member = memberRepository.findByEmail(userNameOrEmail);
            if (member.isEmpty()) {
                log.error("User {} not found", userNameOrEmail);
                throw new RuntimeException("User record not found");
            }
        }
        return member;
    }

    private PasswordResetToken preparePasswordResetToken(Member member) {
        String otp = String.valueOf(secureRandom.nextInt(100000, 900000));
        String token = UUID.randomUUID().toString();
        LocalDateTime tokenExpiryTime = LocalDateTime.now().plusMinutes(passwordResetTokenExpiry);
        return new PasswordResetToken(null, token, otp, member, tokenExpiryTime);
    }

}
