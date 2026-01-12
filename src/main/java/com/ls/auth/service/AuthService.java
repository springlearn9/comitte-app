package com.ls.auth.service;

import com.ls.auth.model.request.LoginRequest;
import com.ls.auth.model.request.RegisterRequest;
import com.ls.auth.model.response.LoginResponse;
import com.ls.auth.model.response.LogoutResponse;
import com.ls.auth.model.response.LoginUserDetails;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        Optional<Member> ou = memberRepository.findByUsernameWithRoles(loginRequest.getUsernameOrEmail());
        if (ou.isEmpty()) ou = memberRepository.findByEmailWithRoles(loginRequest.getUsernameOrEmail());
        Member user = ou.orElseThrow(() -> {
            log.error("Invalid credentials for user: {}", loginRequest.getUsernameOrEmail()); // CHANGE: Log invalid credentials
            return new RuntimeException("invalid credentials");
        });
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.error("Invalid password for user: {}", loginRequest.getUsernameOrEmail()); // CHANGE: Log invalid password
            throw new RuntimeException("invalid credentials");
        }
        log.info("Creating token for user: {}", loginRequest.getUsernameOrEmail());
        byte[] decodedSecret = Base64.getDecoder().decode(jwtSecret);
        
        // Prepare user details
        LoginUserDetails loginUserDetails = prepareLoginUserDetails(user);
        
        // Store all user details in claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", loginUserDetails.getMemberId());
        claims.put("email", loginUserDetails.getEmail());
        claims.put("name", loginUserDetails.getName());
        claims.put("mobile", loginUserDetails.getMobile());
        claims.put("roleIds", new ArrayList<>(loginUserDetails.getGrantedRoleIds()));
        claims.put("roleNames", new ArrayList<>(loginUserDetails.getGrantedRoleNames()));
        claims.put("authorityNames", new ArrayList<>(loginUserDetails.getGrantedAuthorities()));
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, decodedSecret)
                .compact();

        log.info("Login successful for user: {}", user.getUsername());

        // Initialize session tracking
        tokenBlacklistService.updateActivity(token);
        return new LoginResponse(token, "Bearer", jwtExpirationMs, loginUserDetails);
    }

    public LoginUserDetails prepareLoginUserDetails(Member member) {
        Set<Long> grantedRoleIds = getRolesIdsSet(member.getRoles());
        Set<String> grantedRoleNames = getRolesSet(member.getRoles());
        Set<String> grantedAuthorities = getAuthoritiesSet(member.getRoles());
        Set<String> rolesAndAuthorities = new HashSet<String>();
        rolesAndAuthorities.addAll(grantedRoleNames);
        rolesAndAuthorities.addAll(grantedAuthorities);
        Map<Long, String> grantedRoleMap = getRoleMap(member.getRoles());
        return new LoginUserDetails(member.getMemberId(), member.getUsername(), member.getEmail(), member.getName(), member.getMobile(),
                grantedRoleIds, grantedRoleNames, grantedAuthorities,
                (Set<GrantedAuthority>) getGrantedAuthorities(rolesAndAuthorities));
    }

    private final Set<GrantedAuthority> getGrantedAuthorities(Set<String> rolesAndAuthorities) {
        return rolesAndAuthorities.stream().map(p -> new SimpleGrantedAuthority(p)).collect(Collectors.toSet());
    }

    /*
     * Returns Set<String> all roles names assigned to logged in user
     */
    private final Set<Long> getRolesIdsSet(final Set<Role> roles) {
        return roles.stream().map(r -> r.getRoleId()).collect(Collectors.toSet());
    }

    /*
     * Returns Set<String> all roles names assigned to logged in user
     */
    private final Set<String> getRolesSet(final Set<Role> roles) {
        return roles.stream().map(r -> "ROLE_" + r.getRoleName()).collect(Collectors.toSet());
    }

    /*
     * Returns Set<String> all privileges names mapped with logged in user assigned
     * roles
     */
    private final Set<String> getAuthoritiesSet(final Set<Role> roles) {
        Set<String> authorities = roles.stream().flatMap(r -> r.getAuthorities().stream().map(p -> p.getAuthorityName()))
                .collect(Collectors.toSet());
        return authorities;
    }

    private final Map<Long, String> getRoleMap(final Set<Role> roles) {
        Map<Long, String> roleMap = roles.stream().collect(Collectors.toMap(x-> x.getRoleId(), x-> "ROLE_"+x.getRoleName()));
        return roleMap;
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
     * Extract LoginUserDetails from JWT token claims (without DB call)
     */
    @SuppressWarnings("unchecked")
    public LoginUserDetails extractUserDetailsFromToken(String token) {
        byte[] decodedSecret = Base64.getDecoder().decode(jwtSecret);
        var claims = Jwts.parser()
                .setSigningKey(decodedSecret)
                .parseClaimsJws(token)
                .getBody();
        
        // Extract all fields from claims
        Long memberId = claims.get("memberId", Long.class);
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String mobile = claims.get("mobile", String.class);
        
        // Extract collections from claims
        List<Integer> roleIdsInt = (List<Integer>) claims.get("roleIds");
        Set<Long> roleIds = roleIdsInt != null ? roleIdsInt.stream().map(Long::valueOf).collect(Collectors.toSet()) : new HashSet<>();
        
        List<String> roleNamesList = (List<String>) claims.get("roleNames");
        Set<String> roleNames = roleNamesList != null ? new HashSet<>(roleNamesList) : new HashSet<>();
        
        List<String> authorityNamesList = (List<String>) claims.get("authorityNames");
        Set<String> authorityNames = authorityNamesList != null ? new HashSet<>(authorityNamesList) : new HashSet<>();
        
        // Combine role names and authority names for granted authorities
        Set<String> allAuthorities = new HashSet<>();
        allAuthorities.addAll(roleNames);
        allAuthorities.addAll(authorityNames);
        
        Set<GrantedAuthority> grantedAuthorities = allAuthorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        
        // Reconstruct LoginUserDetails without DB call
        return new LoginUserDetails(memberId, username, email, name, mobile,
                roleIds, roleNames, authorityNames, grantedAuthorities);
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
     * Get currently logged-in user's details from SecurityContext (without DB call)
     * Returns LoginUserDetails with all user information from the current authentication context
     */
    public LoginUserDetails getCurrentUserDetails() {
        org.springframework.security.core.Authentication authentication = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("No authenticated user found");
        }
        
        // Principal is now LoginUserDetails (set by JwtAuthenticationFilter)
        if (authentication.getPrincipal() instanceof LoginUserDetails) {
            return (LoginUserDetails) authentication.getPrincipal();
        }
        
        throw new RuntimeException("Invalid principal type");
    }
}
