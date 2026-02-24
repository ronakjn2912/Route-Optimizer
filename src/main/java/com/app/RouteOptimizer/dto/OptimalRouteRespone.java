package com.app.RouteOptimizer.dto;

import lombok.Data;

import java.util.List;

@Data
public class OptimalRouteRespone {
    private final List<Integer> HubIds;
    private final Double totalDistance;
    private final Double totalTime;

    public String toString(){
        return "Optimal route is : "+HubIds+" with total distance "+totalDistance+" and total time "+totalTime;
    }
}
