package com.ls.auth.repository;

import com.ls.auth.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);
    
    /**
     * Find member by username with roles and authorities eagerly fetched
     * This prevents N+1 query issues during authentication
     */
    @Query("SELECT DISTINCT m FROM Member m " +
           "LEFT JOIN FETCH m.roles r " +
           "LEFT JOIN FETCH r.authorities " +
           "WHERE m.username = :username")
    Optional<Member> findByUsernameWithRoles(@Param("username") String username);
    
    /**
     * Find member by email with roles and authorities eagerly fetched
     * This prevents N+1 query issues during authentication
     */
    @Query("SELECT DISTINCT m FROM Member m " +
           "LEFT JOIN FETCH m.roles r " +
           "LEFT JOIN FETCH r.authorities " +
           "WHERE m.email = :email")
    Optional<Member> findByEmailWithRoles(@Param("email") String email);
    
    /**
     * Find members whose name or mobile contains the given value (LIKE %value%).
     * This performs a case-insensitive contains match on name and a contains match on mobile.
     *
     * Example usage: repository.findByNameContainingIgnoreCaseOrMobileContaining("john", "9876");
     */
    List<Member> findByNameContainingIgnoreCaseOrMobileContaining(String name, String mobile);
}