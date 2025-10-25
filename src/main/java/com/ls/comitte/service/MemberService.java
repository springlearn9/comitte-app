package com.ls.comitte.service;

import com.ls.comitte.util.ServiceUtil;
import com.ls.comitte.util.ResponseMapper;
import com.ls.comitte.model.request.MemberRequest;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.auth.model.request.RoleAssignDto;
import com.ls.comitte.model.entity.Member;
import com.ls.auth.model.entity.Role;
import com.ls.comitte.repository.MemberRepository;
import com.ls.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final ResponseMapper mapper = ResponseMapper.INSTANCE;
    private static final String MEMBER_NOT_FOUND = "Member not found";
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;


    public MemberResponse get(Long id) {
        return memberRepository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberResponse create(MemberRequest memberRequest) {
        Member member = mapper.toEntity(memberRequest);
        memberRepository.save(member);
        return mapper.toResponse(member);
    }

    @Transactional
    public MemberResponse update(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException(MEMBER_NOT_FOUND));
        ServiceUtil.update(member, memberRequest);
        memberRepository.save(member);
        return mapper.toResponse(member);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public MemberResponse assignRoles(Long id, RoleAssignDto dto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException(MEMBER_NOT_FOUND));
        Set<Role> roles = dto.getRoleNames().stream().map(rn -> roleRepository.findByRoleName(rn)
                .orElseThrow(() -> new RuntimeException(MEMBER_NOT_FOUND + rn))).collect(Collectors.toSet());
        member.setRoles(roles);
        memberRepository.save(member);
        return mapper.toResponse(member);
    }

    public List<MemberResponse> searchMembers(String name, String mobile) {
        List<Member> members = memberRepository.findByNameContainingIgnoreCaseOrMobileContaining(name, mobile);
        return members.stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}