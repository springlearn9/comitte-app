package com.ls.comitte.repository;

import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComitteMemberMapRepository extends JpaRepository<ComitteMemberMap, Long> {

    @Query("SELECT m FROM Member m JOIN ComitteMemberMap cmm ON m.memberId = cmm.member.memberId WHERE cmm.comitte.comitteId = :comitteId")
    List<Member> findMembersByComitteId(Long comitteId);

    // Find ComitteMemberMap entities by comitte ID with related entities eagerly fetched
    // To find all associated members for a comitte
    @Query("SELECT cmm FROM ComitteMemberMap cmm " +
           "LEFT JOIN FETCH cmm.comitte c " +
           "LEFT JOIN FETCH c.owner " +
           "LEFT JOIN FETCH cmm.member " +
           "WHERE cmm.comitte.comitteId = :comitteId")
    List<ComitteMemberMap> findByComitteIdWithDetails(Long comitteId);

}
