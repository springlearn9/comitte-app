package com.ls.construction.repository;

import com.ls.construction.model.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByProjectProjectId(Long projectId);
}
