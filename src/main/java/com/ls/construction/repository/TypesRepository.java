package com.ls.construction.repository;

import com.ls.construction.model.entity.Types;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypesRepository extends JpaRepository<Types, Long> {
    List<Types> findByModuleName(String moduleName);
    List<Types> findByModuleNameAndCategory(String moduleName, String category);
}
