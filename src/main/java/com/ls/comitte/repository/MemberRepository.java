package com.ls.comitte.repository;

import com.ls.comitte.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);
    List<Member> findByNameOrMobileOrUsername(String name, String mobile, String username);
}
