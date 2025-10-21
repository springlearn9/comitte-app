package com.ls.comitte.repository;

import com.ls.comitte.model.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByComitteId(Long comitteId);
    Long countByComitteId(Long comitteId);
}
