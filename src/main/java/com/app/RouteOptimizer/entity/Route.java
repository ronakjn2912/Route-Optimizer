package com.app.RouteOptimizer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Route {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_hub_id", nullable = false)
    private Hub sourceHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_hub_id", nullable = false)
    private Hub destinationHub;

    private Double distance;
    private Double time;

    public Route(Integer id, Hub sourceHub, Hub destinationHub, Double distance, Double time) {
        this.id = id;
        this.sourceHub = sourceHub;
        this.destinationHub = destinationHub;
        this.distance = distance;
        this.time = time;
    }
}