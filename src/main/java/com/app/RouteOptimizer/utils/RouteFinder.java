package com.app.RouteOptimizer.utils;

import com.app.RouteOptimizer.dto.OptimalRouteRespone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class RouteFinder {
    public OptimalRouteRespone shortest(int sourceHub, int destinationHub, HashMap<Integer, List<GraphNode>> graph, HashMap<Integer, Double> dist){
        log.info("Starting with calculating shortest path from : {} to : {}", sourceHub, destinationHub);

        PriorityQueue<GraphNode> toVisit = new PriorityQueue<>(Comparator.comparingDouble(o -> (o.getDistance() + o.getTime())));
        toVisit.add(new GraphNode(sourceHub, (double)0, (double)0));

        //predecessors - to record path and then we'll trace it backwards from target hub to source
        HashMap<Integer, GraphNode> predecessor = new HashMap<>();//to-from


        do{
            GraphNode current = toVisit.poll();
            if (!graph.containsKey(current.getHubId())){
                continue;
            }
            List<GraphNode> connectedNodes = graph.get(current.getHubId());
            for (GraphNode node : connectedNodes){
                Double newDistance = current.getDistance() +  node.getDistance();
                Double newTime = current.getTime() + node.getTime();
                if (dist.get(node.getHubId())> newDistance+newTime){//if shorter distance is found
                    dist.put(node.getHubId(), newDistance+newTime);//update Distance map
                    toVisit.add(new GraphNode(node.getHubId(), newDistance, newTime));//update PQ
                    predecessor.put(node.getHubId(), new GraphNode(current.getHubId(), newDistance, newTime));//node which provided least cost
                }
            }
        }while (!toVisit.isEmpty());
        log.info("calculated shortest path IDs: {}", predecessor );//each node and it's previous closest connected hub
        int isSource = destinationHub;
        List<Integer> path = new ArrayList<>();
        path.add(destinationHub);
        while (isSource!=sourceHub){
            if (!predecessor.containsKey(isSource)){
                break;
            }
            isSource = predecessor.get(isSource).getHubId();
            path.add(0, isSource);
        }
        Double totalDistance = 0.0;
        Double totalTime = 0.0;
        if (isSource!=sourceHub){
            path = List.of();
            return new OptimalRouteRespone(path, totalDistance, totalTime);
        }
        totalDistance=predecessor.get(destinationHub).getDistance();
        totalTime=predecessor.get(destinationHub).getTime();
        log.info("Returning the shortest path with total distance and time");
        return new OptimalRouteRespone(path, totalDistance, totalTime);
    }
}
