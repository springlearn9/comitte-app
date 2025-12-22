package com.ls.auth.service;

import com.ls.auth.model.request.LoginRequest;
import com.ls.auth.model.request.RegisterRequest;
import com.ls.auth.model.response.LoginResponse;
import com.ls.auth.model.response.LogoutResponse;
import com.ls.auth.model.response.MemberResponse;
import com.ls.auth.model.entity.Member;
import com.ls.auth.model.entity.Role;
import com.ls.auth.repository.MemberRepository;
import com.ls.auth.repository.RoleRepository;
import com.ls.auth.util.AuthMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private AuthMapper mapper = AuthMapper.INSTANCE;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Transactional
    public MemberResponse register(RegisterRequest registerRequest) {
        log.info("Registering user with username: {}", registerRequest.getUsername()); // CHANGE: Log registration attempt
        if (memberRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            log.error("Username {} already exists", registerRequest.getUsername()); // CHANGE: Log error for existing username
            throw new RuntimeException("username exists");
        }
        if (memberRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.error("Email {} already exists", registerRequest.getEmail()); // CHANGE: Log error for existing email
            throw new RuntimeException("email exists");
        }
        Member member = Member.builder().username(registerRequest.getUsername()).email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .mobile(registerRequest.getMobile()).build();
        Role role = roleRepository.findByRoleName("MEMBER").orElse(null);
        Set<Role> roles = new HashSet<>();
        if (role != null) roles.add(role);
        member.setRoles(roles);
        memberRepository.save(member);
        log.info("User registered successfully with username: {}", registerRequest.getUsername()); // CHANGE: Log successful registration
        return mapper.toResponse(member);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getUsernameOrEmail()); // CHANGE: Log login attempt
        Optional<Member> ou = memberRepository.findByUsername(loginRequest.getUsernameOrEmail());
        if (ou.isEmpty()) ou = memberRepository.findByEmail(loginRequest.getUsernameOrEmail());
        Member user = ou.orElseThrow(() -> {
            log.error("Invalid credentials for user: {}", loginRequest.getUsernameOrEmail()); // CHANGE: Log invalid credentials
            return new RuntimeException("invalid credentials");
        });
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.error("Invalid password for user: {}", loginRequest.getUsernameOrEmail()); // CHANGE: Log invalid password
            throw new RuntimeException("invalid credentials");
        }
        log.info("Creating token for user: {}", loginRequest.getUsernameOrEmail());
        byte[] decodedSecret = Base64.getDecoder().decode(jwtSecret); // CHANGE: Decode the Base64 secret
        String token = Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, decodedSecret).compact(); // CHANGE: Use decoded secret
        MemberResponse memberResponse = mapper.toResponse(user);
        log.info("Login successful for user: {}", user.getUsername()); // CHANGE: Log successful login
        
        // Initialize session tracking
        tokenBlacklistService.updateActivity(token);
        
        return new LoginResponse(token, "Bearer", jwtExpirationMs, memberResponse);
    }
    
    /**
     * Logout user by blacklisting their token
     */
    public LogoutResponse logout(String token) {
        log.info("Processing logout request");
        tokenBlacklistService.blacklistToken(token);
        return new LogoutResponse("Logged out successfully", System.currentTimeMillis());
    }
    
    /**
     * Extract username from JWT token
     */
    public String extractUsername(String token) {
        byte[] decodedSecret = Base64.getDecoder().decode(jwtSecret);
        return Jwts.parser()
                .setSigningKey(decodedSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return tokenUsername.equals(username);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract roles from member
     */
    public List<String> getRolesByUsername(String username) {
        Member member = memberRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return member.getRoles().stream()
                .map(role -> "ROLE_" + role.getRoleName())
                .toList();
    }
}
