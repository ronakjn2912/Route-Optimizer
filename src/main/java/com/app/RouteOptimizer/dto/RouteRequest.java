package com.app.RouteOptimizer.dto;

import com.app.RouteOptimizer.entity.Hub;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {
    private int id;
    private Hub sourceHub;
    private Hub destinationHub;

    @NotNull(message = "Distance cannot be empty")
    @PositiveOrZero(message = "Distance should be positive")
    private Double distance;
    @NotNull(message = "Time is mandatory field")
    private Double time;
}
