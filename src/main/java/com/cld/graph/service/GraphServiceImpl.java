package com.cld.graph.service;

import com.cld.graph.entity.GraphEntity;
import com.cld.graph.entity.RelationshipEntity;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.cld.graph.grpc.KnowledgeGraphServiceGrpc;
import com.cld.graph.grpc.CreateGraphResponse;
import com.cld.graph.grpc.CreateGraphRequest;
import com.cld.graph.grpc.CreateRelationshipRequest;
import com.cld.graph.grpc.CreateRelationshipResponse;
import com.cld.graph.grpc.QueryGraphsRequest;
import com.cld.graph.grpc.QueryGraphsResponse;
import com.cld.graph.grpc.QueryRelationshipsRequest;
import com.cld.graph.grpc.QueryRelationshipsResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService
public class GraphServiceImpl extends KnowledgeGraphServiceGrpc.KnowledgeGraphServiceImplBase {

    private final GraphService graphService;

    public GraphServiceImpl(GraphService graphService) {
        this.graphService = graphService;
    }

    @Override
    public void createGraph(CreateGraphRequest request, StreamObserver<CreateGraphResponse> responseObserver) {
        GraphEntity entity = GraphEntity.builder()
                .name(request.getName())
                .type(request.getType())
                .properties(new HashMap<>(request.getPropertiesMap()))
                .build();

        GraphEntity savedGraph = graphService.createGraph(entity);

        CreateGraphResponse response = CreateGraphResponse.newBuilder()
                .setGraph(convertToGrpc(savedGraph))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createRelationship(CreateRelationshipRequest request, StreamObserver<CreateRelationshipResponse> responseObserver) {
        RelationshipEntity entity = RelationshipEntity.builder()
                .sourceGraphId(request.getSourceGraphId())
                .targetGraphId(request.getTargetGraphId())
                .type(request.getType())
                .properties(new HashMap<>(request.getPropertiesMap()))
                .build();

        RelationshipEntity savedRel = graphService.createRelationship(entity);

        CreateRelationshipResponse response = CreateRelationshipResponse.newBuilder()
                .setRelationship(convertToGrpc(savedRel))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getGraphs(QueryGraphsRequest request, StreamObserver<QueryGraphsResponse> responseObserver) {
        var graphs = graphService.getAllGraphs().stream()
                .filter(g -> request.getName().isEmpty() || g.getName().equals(request.getName()))
                .filter(g -> request.getType().isEmpty() || g.getType().equals(request.getType()))
                .map(this::convertToGrpc)
                .collect(Collectors.toList());

        QueryGraphsResponse response = QueryGraphsResponse.newBuilder()
                .addAllGraphs(graphs)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getRelationships(QueryRelationshipsRequest request, StreamObserver<QueryRelationshipsResponse> responseObserver) {
        var rels = graphService.getRelationshipsFromGraph(request.getSourceGraphId()).stream()
                .filter(r -> request.getTargetGraphId() == 0 || r.getTargetGraphId().equals(request.getTargetGraphId()))
                .filter(r -> request.getType().isEmpty() || r.getType().equals(request.getType()))
                .map(this::convertToGrpc)
                .collect(Collectors.toList());

        QueryRelationshipsResponse response = QueryRelationshipsResponse.newBuilder()
                .addAllRelationships(rels)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private com.cld.graph.grpc.Graph convertToGrpc(GraphEntity g) {
        return com.cld.graph.grpc.Graph.newBuilder()
                .setId(g.getId())
                .setName(g.getName())
                .setType(g.getType())
                .putAllProperties(g.getProperties().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())))
                .build();
    }

    private com.cld.graph.grpc.Relationship convertToGrpc(RelationshipEntity r) {
        return com.cld.graph.grpc.Relationship.newBuilder()
                .setId(r.getId())
                .setSourceGraphId(r.getSourceGraphId())
                .setTargetGraphId(r.getTargetGraphId())
                .setType(r.getType())
                .putAllProperties(r.getProperties().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())))
                .build();
    }
}