package com.cld.graph.repository;

import com.cld.graph.entity.RelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<RelationshipEntity, Long> {
    List<RelationshipEntity> findBySourceGraphId(Long sourceNodeId);
    List<RelationshipEntity> findByTargetGraphId(Long targetNodeId);
}
