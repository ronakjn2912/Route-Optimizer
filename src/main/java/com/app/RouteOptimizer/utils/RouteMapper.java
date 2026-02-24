package com.app.RouteOptimizer.utils;

import com.app.RouteOptimizer.dto.RouteRequest;
import com.app.RouteOptimizer.dto.RouteResponse;
import com.app.RouteOptimizer.entity.Route;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper {
    public Route toEntity(RouteRequest routeRequestDto){
        Route route = new Route(routeRequestDto.getId(), routeRequestDto.getSourceHub(), routeRequestDto.getDestinationHub(), routeRequestDto.getDistance(), routeRequestDto.getTime());

        return route;
    }

    public RouteResponse toDto(Route routeEntity){
        RouteResponse route = new RouteResponse();
        route.setId(routeEntity.getId());
        route.setSourceHub(routeEntity.getSourceHub());
        route.setDestinationHub(routeEntity.getDestinationHub());
        route.setDistance(routeEntity.getDistance());
        route.setTime(routeEntity.getTime());

        return route;
    }
}
