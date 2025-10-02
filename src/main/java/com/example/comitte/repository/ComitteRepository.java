package com.example.comitte.repository;

import com.example.comitte.model.entity.Comitte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComitteRepository extends JpaRepository<Comitte, Long> {

    // find all comittes where a member belongs
    @Query("SELECT c FROM Comitte c, ComitteMemberMap cmm WHERE c.comitteId = cmm.comitteId AND cmm.memberId = :memberId")
    List<Comitte> findComittesByMemberId(@Param("memberId") Long memberId);

}
