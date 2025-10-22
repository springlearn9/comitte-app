package com.ls.comitte.repository;

import com.ls.comitte.model.entity.Comitte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComitteRepository extends JpaRepository<Comitte, Long> {

    // find all comittes where a member belongs
    @Query("SELECT DISTINCT c FROM Comitte c JOIN ComitteMemberMap cmm ON c = cmm.comitte WHERE cmm.member.memberId = :memberId")
    List<Comitte> findComittesByMemberId(@Param("memberId") Long memberId);

    // find all comittes for a owner
    @Query("SELECT c FROM Comitte c WHERE c.owner.memberId = :ownerId")
    List<Comitte> findComittesByOwnerId(@Param("ownerId") Long ownerId);
}
