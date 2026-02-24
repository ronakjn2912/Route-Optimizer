package com.app.RouteOptimizer.utils;

import lombok.Data;

@Data
public class GraphNode {
    private final int hubId;
    private final Double distance;
    private final Double time;
    //private final Double cost;//distance+time

    public String toString(){
        return "destination id : "+ hubId +" distance : "+distance+" time : "+time;
    }
}
