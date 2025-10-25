package com.ls.comitte.repository;

import com.ls.comitte.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);
    
    /**
     * Find members whose name or mobile contains the given value (LIKE %value%).
     * This performs a case-insensitive contains match on name and a contains match on mobile.
     *
     * Example usage: repository.findByNameContainingIgnoreCaseOrMobileContaining("john", "9876");
     */
    List<Member> findByNameContainingIgnoreCaseOrMobileContaining(String name, String mobile);
}
