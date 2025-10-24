package com.ls.comitte.repository;

import com.ls.comitte.model.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    
    // Get all bids for a specific committee with all related entities eagerly fetched
    @Query("SELECT DISTINCT b FROM Bid b " +
           "LEFT JOIN FETCH b.receiversList " +
           "LEFT JOIN FETCH b.comitte c " +
           "LEFT JOIN FETCH c.owner " +
           "LEFT JOIN FETCH b.finalBidder " +
           "WHERE b.comitte.comitteId = :comitteId " +
           "ORDER BY b.bidDate DESC")
    List<Bid> findByComitteIdWithDetails(@Param("comitteId") Long comitteId);
    
    // Get all bids for committees where a member belongs with essential data only (no roles/authorities)
    @Query("SELECT DISTINCT b FROM Bid b " +
           "LEFT JOIN FETCH b.receiversList " +
           "LEFT JOIN FETCH b.comitte c " +
           "LEFT JOIN FETCH c.owner " +
           "LEFT JOIN FETCH b.finalBidder " +
           "JOIN ComitteMemberMap cmm ON b.comitte = cmm.comitte " +
           "WHERE cmm.member.memberId = :memberId " +
           "ORDER BY b.bidDate DESC")
    List<Bid> findBidsForMemberCommittees(@Param("memberId") Long memberId);
    
    // Count existing bids for a committee to calculate next comitteNumber
    Integer countByComitte_ComitteId(Long comitteId);
}
