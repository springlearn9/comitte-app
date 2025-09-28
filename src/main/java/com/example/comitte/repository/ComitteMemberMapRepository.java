package com.example.comitte.repository;

import com.example.comitte.model.entity.ComitteMemberId;
import com.example.comitte.model.entity.ComitteMemberMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComitteMemberMapRepository extends JpaRepository<ComitteMemberMap, ComitteMemberId> {
    List<ComitteMemberMap> findByMemberMemberId(Long memberId);
    List<ComitteMemberMap> findByComitteComitteId(Long comitteId);
}
