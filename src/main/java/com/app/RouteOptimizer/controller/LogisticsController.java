package com.app.RouteOptimizer.controller;

import com.app.RouteOptimizer.dto.HubDto;
import com.app.RouteOptimizer.dto.OptimalRouteRespone;
import com.app.RouteOptimizer.dto.RouteRequest;
import com.app.RouteOptimizer.dto.RouteResponse;
import com.app.RouteOptimizer.entity.Hub;
import com.app.RouteOptimizer.entity.Route;
import com.app.RouteOptimizer.service.LogisticsService;
import com.app.RouteOptimizer.utils.RouteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService  logisticsService;
    private final RouteMapper routeMapper;

    @PostMapping("/hubs")// Avg response time : 209ms with 500 api calls
    public ResponseEntity<Hub> createHub(@Valid @RequestBody HubDto newHub) {//@Valid triggers methodArgumentNotValid exception
//        should be dto instead of entity itself, as the client should not be allowed to manipulate or set id manually
        //exception handled by global exception
        Hub response = logisticsService.createHub(newHub);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/hubs")
    public ResponseEntity<List<Hub>> getHubs(){
        List<Hub> response = logisticsService.getHubs();
        return new ResponseEntity<>(response, HttpStatus.valueOf(201));
    }

    @PostMapping("/route")// Avg response time : 172ms with 800 api calls
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody RouteRequest newRoute) {
        Route response = logisticsService.createRoute(newRoute);
        RouteResponse routeRequestDto = routeMapper.toDto(response);
        return new ResponseEntity<>(routeRequestDto, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/shortest-path")// Avg response time : 27 with 214 api calls
    public ResponseEntity<OptimalRouteRespone> getOptimalRoute(@RequestParam int sourceId, @RequestParam int destinationId){
        OptimalRouteRespone response =  logisticsService.optimizedRoute(sourceId, destinationId );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }


}