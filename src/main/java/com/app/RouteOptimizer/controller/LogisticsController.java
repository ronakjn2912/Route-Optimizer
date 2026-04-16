package com.app.RouteOptimizer.controller;

import com.app.RouteOptimizer.dto.OptimalRouteRespone;
import com.app.RouteOptimizer.dto.RouteRequest;
import com.app.RouteOptimizer.dto.RouteResponse;
import com.app.RouteOptimizer.entity.Hub;
import com.app.RouteOptimizer.entity.Route;
import com.app.RouteOptimizer.service.LogisticsService;
import com.app.RouteOptimizer.utils.RouteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService logisticsService;
    private final RouteMapper routeMapper;

    @PostMapping("/hubs")
    public ResponseEntity<Hub> createHub(@Valid @RequestBody Hub newHub) {//@Valid triggers methodArgumentNotValid exception
        //exception handled by global exception
        Hub response = logisticsService.createHub(newHub);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/hubs")
    public ResponseEntity<List<Hub>> getHubs(){
        List<Hub> response = logisticsService.getHubs();
        return new ResponseEntity<>(response, HttpStatus.valueOf(201));
    }

    @PostMapping("/route")
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody RouteRequest newRoute) {
        Route response = logisticsService.createRoute(newRoute);
        RouteResponse routeRequestDto = routeMapper.toDto(response);
        return new ResponseEntity<>(routeRequestDto, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<OptimalRouteRespone> getOptimalRoute(@RequestParam int sourceId, @RequestParam int destinationId){
        OptimalRouteRespone response =  logisticsService.optimizedRoute(sourceId, destinationId );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
}