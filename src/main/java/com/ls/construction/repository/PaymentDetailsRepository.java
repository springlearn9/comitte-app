package com.ls.construction.repository;

import com.ls.construction.model.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    List<PaymentDetails> findByProjectProjectId(Long projectId);
}
