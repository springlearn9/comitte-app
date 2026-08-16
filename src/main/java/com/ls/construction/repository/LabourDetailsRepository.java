package com.ls.construction.repository;

import com.ls.construction.model.entity.LabourDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabourDetailsRepository extends JpaRepository<LabourDetails, Long> {
    List<LabourDetails> findByProjectProjectId(Long projectId);
}
