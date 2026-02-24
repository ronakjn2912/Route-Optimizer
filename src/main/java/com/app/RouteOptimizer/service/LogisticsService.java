package com.app.RouteOptimizer.service;

import com.app.RouteOptimizer.dto.OptimalRouteRespone;
import com.app.RouteOptimizer.dto.RouteRequest;
import com.app.RouteOptimizer.entity.Hub;
import com.app.RouteOptimizer.entity.Route;
import com.app.RouteOptimizer.exception.HubAlreadyExistsException;
import com.app.RouteOptimizer.exception.HubDoesNotExistsException;
import com.app.RouteOptimizer.repository.HubRepository;
import com.app.RouteOptimizer.repository.RouteRepository;
import com.app.RouteOptimizer.utils.GraphNode;
import com.app.RouteOptimizer.utils.RouteFinder;
import com.app.RouteOptimizer.utils.RouteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogisticsService {

    private final HubRepository hubRepository;
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final RouteFinder routeFinder;

    public Hub createHub(Hub hub){
        log.info("Attempting to create a new hub with location code: {}", hub.getLocationCode());
        //hub already exists
        if (hubRepository.existsByLocationCode(hub.getLocationCode())){
            log.warn("Hub creation failed: Location code {} already exists", hub.getLocationCode());
            throw new HubAlreadyExistsException("Hub Already Exists");
        }
        Hub savedHub = hubRepository.save(hub);
        log.info("Successfully created hub with ID: {}", savedHub.getId());
        return savedHub;
    }

    public Route createRoute(RouteRequest route) {
        log.info("Attempting to create a new route from Source Hub ID : {} to Destination Hub ID : {}", route.getSourceHub(), route.getDestinationHub());
        if(!hubRepository.existsById(route.getSourceHub().getId()) || !hubRepository.existsById(route.getDestinationHub().getId())){
            log.warn("Make sure both hubs are created before creation of route between them");
            throw new HubDoesNotExistsException("One or both hubs don't exist!");
        }
        Route savedRoute = routeRepository.save(routeMapper.toEntity(route));//DTO to entity
        log.info("Successfully created route with ID: {}", savedRoute);
        return savedRoute;
    }

    public List<Hub> getHubs(){
        return hubRepository.findAll();
    }

    public OptimalRouteRespone optimizedRoute(int sourceHub, int destinationHub){
        log.info("Attempting to find optimal path between source : {} to destination : {}", sourceHub, destinationHub);
        if(!hubRepository.existsById(sourceHub) || !hubRepository.existsById(destinationHub)){
            log.warn("Make sure both hubs are created before searching for optimal path");
            throw new HubDoesNotExistsException("One or both hubs don't exist!");
        }

        List<Route> routes = routeRepository.findAll();
        List<Hub> hubs = hubRepository.findAll();
        HashMap<Integer, List<GraphNode>> graph = new HashMap<>();// SourceHub Id : {(Dest HubId, Cost)}
        for (Route route : routes){
            List<GraphNode> edges = new ArrayList<>();
            if (graph.containsKey(route.getSourceHub().getId())){
                edges = graph.get(route.getSourceHub().getId());
            }
            edges.add(new GraphNode(route.getDestinationHub().getId(), route.getDistance(), route.getTime()));
            graph.put(route.getSourceHub().getId(), edges);
        }
        log.info("Adjacency list created: {}", graph);

        //store distance from source to *all hubs*
        HashMap<Integer, Double> dist = new HashMap<>();
        for (Hub hub : hubs){
            if (hub.getId()==sourceHub){
                dist.put(sourceHub, (double)0);
                continue;
            }
            //assign infinity to rest of the distances
            dist.put(hub.getId(), Double.MAX_VALUE);
        }
        log.info("Distance Array created");
        return routeFinder.shortest(sourceHub, destinationHub, graph, dist);
    }
}


//edge case - if user tries to add a duplicate edge