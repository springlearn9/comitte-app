package com.ls.comitte.service;

import com.ls.comitte.model.request.MemberRequest;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.auth.model.request.RoleAssignDto;
import com.ls.auth.model.UserUpdateDto;
import com.ls.comitte.model.entity.Member;
import com.ls.auth.model.entity.Role;
import com.ls.comitte.repository.MemberRepository;
import com.ls.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    public MemberResponse get(Long id) {
        return memberRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public MemberResponse create(MemberRequest dto) {
        Member m = Member.builder().name(dto.getName()).mobile(dto.getMobile()).aadharNo(dto.getAadharNo()).address(dto.getAddress()).build();
        memberRepository.save(m);
        return toDto(m);
    }

    @Transactional
    public MemberResponse update(Long id, UserUpdateDto dto) {
        Member u = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setMobile(dto.getMobileNumber());
        memberRepository.save(u);
        return toDto(u);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public MemberResponse assignRoles(Long id, RoleAssignDto dto) {
        Member u = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        Set<Role> roles = dto.getRoleNames().stream().map(rn -> roleRepository.findByRoleName(rn).orElseThrow(() -> new RuntimeException("role not found:" + rn))).collect(Collectors.toSet());
        u.setRoles(roles);
        memberRepository.save(u);
        return toDto(u);
    }


    @Transactional
    public MemberResponse update(Long id, MemberRequest dto) {
        Member m = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        m.setName(dto.getName());
        m.setMobile(dto.getMobile());
        m.setAadharNo(dto.getAadharNo());
        m.setAddress(dto.getAddress());
        memberRepository.save(m);
        return toDto(m);
    }

    private MemberResponse toDto(Member member) {
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setMemberId(member.getMemberId());
        memberResponse.setName(member.getName());
        memberResponse.setEmail(member.getEmail());
        memberResponse.setUsername(member.getUsername());
        memberResponse.setMobile(member.getMobile());
        memberResponse.setAadharNo(member.getAadharNo());
        memberResponse.setAddress(member.getAddress());
        memberResponse.setCreatedTimestamp(member.getCreatedTimestamp());
        memberResponse.setUpdatedTimestamp(member.getUpdatedTimestamp());
        return memberResponse;
    }
}