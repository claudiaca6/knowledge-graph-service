package com.cld.graph.service;

import com.cld.graph.entity.GraphEntity;
import com.cld.graph.entity.RelationshipEntity;
import com.cld.graph.repository.GraphRepository;
import com.cld.graph.repository.RelationshipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    private final GraphRepository graphRepo;
    private final RelationshipRepository relRepo;

    public GraphService(GraphRepository nodeRepo, RelationshipRepository relRepo) {
        this.graphRepo = nodeRepo;
        this.relRepo = relRepo;
    }

    public GraphEntity createGraph(GraphEntity graph) {
        return graphRepo.save(graph);
    }

    public RelationshipEntity createRelationship(RelationshipEntity rel) {
        return relRepo.save(rel);
    }

    public List<GraphEntity> getAllGraphs() {
        return graphRepo.findAll();
    }

    public List<RelationshipEntity> getRelationshipsFromGraph(Long sourcegraphId) {
        return relRepo.findBySourceGraphId(sourcegraphId);
    }
}
