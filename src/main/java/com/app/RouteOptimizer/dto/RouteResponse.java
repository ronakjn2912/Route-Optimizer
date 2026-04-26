package com.app.RouteOptimizer.dto;

import com.app.RouteOptimizer.entity.Hub;
import lombok.Data;

@Data
public class RouteResponse {
    private Integer id;
    private Hub sourceHub;
    private Hub destinationHub;
    private Double distance;
    private Double time;
}