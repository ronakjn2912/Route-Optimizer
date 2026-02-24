package com.app.RouteOptimizer;

import com.app.RouteOptimizer.dto.OptimalRouteRespone;
import com.app.RouteOptimizer.entity.Hub;
import com.app.RouteOptimizer.entity.Route;
import com.app.RouteOptimizer.utils.GraphNode;
import com.app.RouteOptimizer.utils.RouteFinder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RouteFinderTest {

    private RouteFinder routeFinder;

    @BeforeEach
    void setup(){
        routeFinder = new RouteFinder();
    }

    @Test
    void shouldFindShortestPath_whenSimpleGraph() {//Happy Path - tested
        //Arrange all variables needed for shortest path metho
        int testSource = 1, destinationSource = 5;
        HashMap<Integer, List<GraphNode>> graph = new HashMap<>();

        graph.put(1, List.of(new GraphNode(2, 1.5, 1.5), new GraphNode(5, 10.0, 12.0)));
        graph.put(2, List.of(new GraphNode(3, 3.5, 3.5), new GraphNode(4, 1.5, 2.5)));
        graph.put(3, List.of(new GraphNode(5, 2.5, 2.5)));
        graph.put(4, List.of(new GraphNode(5, 4.0, 5.0)));

        HashMap<Integer, Double> dist = new HashMap<>();
        for (int i=1;i<=5;i++){
            if (i==testSource){
                dist.put(i, (double)0);
            }
            dist.put(i, (double)Integer.MAX_VALUE);
        }
        log.info("Arranging done for test");
        //ACT
        OptimalRouteRespone shortestPathResponse = routeFinder.shortest(testSource, destinationSource, graph, dist);
        log.info("shortest path found for dummy routes");
        List<Integer> path = shortestPathResponse.getHubIds();
        Double totalDistance = shortestPathResponse.getTotalDistance();
        Double totalTime = shortestPathResponse.getTotalTime();
        //Assert
        assertThat(path).containsExactly(1, 2, 3, 5);
        assertThat(totalDistance).isEqualTo(7.5);
        assertThat(totalTime).isEqualTo(7.5);
    }

    @Test
    void noPathExists(){//should return empty list when no path exists
//        Arrange, Act, Assert
        //Arrange
        int testSource = 2, destinationSource = 4;

        HashMap<Integer, List<GraphNode>> graph = new HashMap<>();

        graph.put(1, List.of(new GraphNode(2, 1.5, 1.5), new GraphNode(3, 10.0, 12.0)));
        graph.put(3, List.of(new GraphNode(4, 2.5, 2.5)));

        HashMap<Integer, Double> dist = new HashMap<>();
        for (int i=1;i<=5;i++){
            if (i==testSource){
                dist.put(i, (double)0);
            }
            dist.put(i, (double)Integer.MAX_VALUE);
        }

        //Act
        OptimalRouteRespone shortestPathResponse = routeFinder.shortest(testSource, destinationSource, graph, dist);
        List<Integer> path = shortestPathResponse.getHubIds();
        Double totalDistance = shortestPathResponse.getTotalDistance();
        Double totalTime = shortestPathResponse.getTotalTime();
        log.info("shortest path between hubs : {} with distance : {} and time : {}", path, totalDistance, totalTime);

        //Assert
        assertThat(path).containsExactly();//list
        assertThat(totalDistance).isEqualTo(0);//Double
        assertThat(totalTime).isEqualTo(0);//Double
    }

    @Test
    void GraphWithCycle(){//Graph with cycle
        //Arrange
        int testSource = 1, destinationSource = 4;

        HashMap<Integer, List<GraphNode>> graph = new HashMap<>();

        graph.put(1, List.of(new GraphNode(2, 1.5, 1.5), new GraphNode(3, 10.0, 12.0)));
        graph.put(2, List.of(new GraphNode(4, 3.5, 2.5)));
        graph.put(3, List.of(new GraphNode(2, 2.5, 2.5)));
        graph.put(4, List.of(new GraphNode(3, 1.0, 2.5)));

        HashMap<Integer, Double> dist = new HashMap<>();
        for (int i=1;i<=4;i++){
            if (i==testSource){
                dist.put(i, (double)0);
            }
            dist.put(i, (double)Integer.MAX_VALUE);
        }

        //Act
        OptimalRouteRespone shortestPathResponse = routeFinder.shortest(testSource, destinationSource, graph, dist);
        List<Integer> path = shortestPathResponse.getHubIds();
        Double totalDistance = shortestPathResponse.getTotalDistance();
        Double totalTime = shortestPathResponse.getTotalTime();
        log.info("shortest path between hubs : {} with distance : {} and time : {}", path, totalDistance, totalTime);

        //Assert
        assertThat(path).containsExactly(1,2,4);
        assertThat(totalDistance).isEqualTo(5.0);
        assertThat(totalTime).isEqualTo(4.0);
    }

    //all the edge cases for which tests need to be done

    //4. Multiple paths with same cost → pick one with fewest hops
    @Test
    void GraphWithMultiplePaths(){
        //Arrange
        int testSource = 1, destinationSource = 5;

        HashMap<Integer, List<GraphNode>> graph = new HashMap<>();

        graph.put(1, List.of(new GraphNode(2, 2.0, 2.0), new GraphNode(3, 1.0, 2.0)));
        graph.put(2, List.of(new GraphNode(5, 1.5, 1.5)));
        graph.put(3, List.of(new GraphNode(4, 0.5, 0.0)));
        graph.put(4, List.of(new GraphNode(5, 1.0, 2.5)));

        HashMap<Integer, Double> dist = new HashMap<>();
        for (int i=1;i<=5;i++){
            if (i==testSource){
                dist.put(i, (double)0);
            }
            dist.put(i, (double)Integer.MAX_VALUE);
        }

        //Act
        OptimalRouteRespone shortestPathResponse = routeFinder.shortest(testSource, destinationSource, graph, dist);
        List<Integer> path = shortestPathResponse.getHubIds();
        Double totalDistance = shortestPathResponse.getTotalDistance();
        Double totalTime = shortestPathResponse.getTotalTime();
        log.info("shortest path between hubs : {} with distance : {} and time : {}", path, totalDistance, totalTime);

        //Assert
        assertThat(path).containsExactly(1,2,5);
        assertThat(totalDistance).isEqualTo(3.5);
        assertThat(totalTime).isEqualTo(3.5);
    }
}