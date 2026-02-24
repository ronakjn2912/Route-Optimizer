package com.app.RouteOptimizer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Route {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_hub_id", nullable = false)
    private Hub sourceHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_hub_id", nullable = false)
    private Hub destinationHub;

    private Double distance;
    private Double time;

    public Route(int id, Hub sourceHub, Hub destinationHub, Double distance, Double time) {
        this.id = id;
        this.sourceHub = sourceHub;
        this.destinationHub = destinationHub;
        this.distance = distance;
        this.time = time;
    }
}