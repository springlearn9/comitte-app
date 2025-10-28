package com.ls.comitte.repository;

import com.ls.comitte.model.entity.Comitte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComitteRepository extends JpaRepository<Comitte, Long> {

    // find all comittes where a member belongs with bids count
    @Query("SELECT NEW com.ls.comitte.model.entity.Comitte(" +
           "c.comitteId, c.owner, c.comitteName, c.startDate, c.fullAmount, " +
           "c.membersCount, c.fullShare, c.dueDateDays, c.paymentDateDays, " +
           "CAST((SELECT COUNT(b) FROM Bid b WHERE b.comitte = c) AS int), " +
           "c.createdTimestamp, c.updatedTimestamp) " +
           "FROM Comitte c JOIN ComitteMemberMap cmm ON c = cmm.comitte " +
           "WHERE cmm.member.memberId = :memberId")
    List<Comitte> findComittesByMemberIdWithBidsCount(@Param("memberId") Long memberId);

    // find all comittes for a owner with bids count
    @Query("SELECT NEW com.ls.comitte.model.entity.Comitte(" +
           "c.comitteId, c.owner, c.comitteName, c.startDate, c.fullAmount, " +
           "c.membersCount, c.fullShare, c.dueDateDays, c.paymentDateDays, " +
           "CAST((SELECT COUNT(b) FROM Bid b WHERE b.comitte = c) AS int), " +
           "c.createdTimestamp, c.updatedTimestamp) " +
           "FROM Comitte c WHERE c.owner.memberId = :ownerId")
    List<Comitte> findComittesByOwnerIdWithBidsCount(@Param("ownerId") Long ownerId);

    // find single comitte by ID with bids count
    @Query("SELECT NEW com.ls.comitte.model.entity.Comitte(" +
           "c.comitteId, c.owner, c.comitteName, c.startDate, c.fullAmount, " +
           "c.membersCount, c.fullShare, c.dueDateDays, c.paymentDateDays, " +
           "CAST((SELECT COUNT(b) FROM Bid b WHERE b.comitte = c) AS int), " +
           "c.createdTimestamp, c.updatedTimestamp) " +
           "FROM Comitte c WHERE c.comitteId = :comitteId")
    Optional<Comitte> findByIdWithBidsCount(@Param("comitteId") Long comitteId);
}
