package com.cld.graph.repository;

import com.cld.graph.entity.GraphEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends JpaRepository<GraphEntity, Long> {
}
