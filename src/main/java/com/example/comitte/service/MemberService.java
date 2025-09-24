package com.example.comitte.service;

import com.example.comitte.model.dto.member.MemberCreateDto;
import com.example.comitte.model.dto.member.MemberDto;
import com.example.comitte.model.dto.role.RoleAssignDto;
import com.example.comitte.model.dto.user.UserUpdateDto;
import com.example.comitte.model.entity.Member;
import com.example.comitte.model.entity.Role;
import com.example.comitte.repository.MemberRepository;
import com.example.comitte.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    public Page<MemberDto> list(Pageable p) {
        var pg = memberRepository.findAll(p);
        List<MemberDto> l = pg.stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(l, p, pg.getTotalElements());
    }

    public MemberDto get(Long id) {
        return memberRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public MemberDto create(MemberCreateDto dto) {
        Member m = Member.builder().name(dto.getName()).mobile(dto.getMobile()).aadharNo(dto.getAadharNo()).address(dto.getAddress()).build();
        memberRepository.save(m);
        return toDto(m);
    }

    @Transactional
    public MemberDto update(Long id, UserUpdateDto dto) {
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
    public MemberDto assignRoles(Long id, RoleAssignDto dto) {
        Member u = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        Set<Role> roles = dto.getRoleNames().stream().map(rn -> roleRepository.findByRoleName(rn).orElseThrow(() -> new RuntimeException("role not found:" + rn))).collect(Collectors.toSet());
        u.setRoles(roles);
        memberRepository.save(u);
        return toDto(u);
    }


    @Transactional
    public MemberDto update(Long id, MemberCreateDto dto) {
        Member m = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        m.setName(dto.getName());
        m.setMobile(dto.getMobile());
        m.setAadharNo(dto.getAadharNo());
        m.setAddress(dto.getAddress());
        memberRepository.save(m);
        return toDto(m);
    }

    private MemberDto toDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setName(member.getName());
        memberDto.setEmail(member.getEmail());
        memberDto.setUsername(member.getUsername());
        memberDto.setMobile(member.getMobile());
        memberDto.setAadharNo(member.getAadharNo());
        memberDto.setAddress(member.getAddress());
        memberDto.setCreatedTimestamp(member.getCreatedTimestamp());
        memberDto.setUpdatedTimestamp(member.getUpdatedTimestamp());
        return memberDto;
    }
}