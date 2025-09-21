package com.example.comitte.service;

import com.example.comitte.dto.auth.RegisterRequestDto;
import com.example.comitte.dto.auth.LoginRequestDto;
import com.example.comitte.dto.auth.LoginResponseDto;
import com.example.comitte.dto.member.MemberDto;
import com.example.comitte.entity.Member;
import com.example.comitte.entity.Role;
import com.example.comitte.repository.MemberRepository;
import com.example.comitte.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;
    private final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public MemberDto register(RegisterRequestDto dto) {
        if (memberRepository.findByUsername(dto.getUsername()).isPresent()) throw new RuntimeException("username exists");
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) throw new RuntimeException("email exists");
        Member u = Member.builder().username(dto.getUsername()).email(dto.getEmail())
                //.password(passwordEncoder.encode(dto.getPassword()))
                .mobile(dto.getMobile()).build();
        // assign MEMBER role by default
        Role r = roleRepository.findByRoleName("MEMBER").orElse(null);
        Set<Role> roles = new HashSet<>();
        if (r != null) roles.add(r);
        u.setRoles(roles);
        memberRepository.save(u);
        return toDto(u);
    }

/*    public LoginResponseDto login(LoginRequestDto dto) {
        Optional<Member> ou = memberRepository.findByUsername(dto.getUsernameOrEmail());
        if (ou.isEmpty()) ou = memberRepository.findByEmail(dto.getUsernameOrEmail());
        Member u = ou.orElseThrow(() -> new RuntimeException("invalid credentials"));
        if (!passwordEncoder.matches(dto.getPassword(), u.getPassword()))
            throw new RuntimeException("invalid credentials");
        String token = Jwts.builder().setSubject(u.getUsername()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
        MemberDto ud = toDto(u);
        return new LoginResponseDto(token, "Bearer", jwtExpirationMs, ud);
    }*/

    private MemberDto toDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setUsername(member.getUsername());
        memberDto.setEmail(member.getEmail());
        memberDto.setMobile(member.getMobile());
        memberDto.setRoles(member.getRoles() == null ? null : member.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        return memberDto;
    }
}