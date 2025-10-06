package com.cld.graph.entity;

import com.cld.graph.converter.JsonbConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "relationship")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_graph_id", nullable = false)
    private Long sourceGraphId;

    @Column(name = "target_graph_id", nullable = false)
    private Long targetGraphId;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private Map<String, Object> properties;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

