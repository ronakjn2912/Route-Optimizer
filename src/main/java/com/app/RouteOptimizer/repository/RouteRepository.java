package com.app.RouteOptimizer.repository;

import com.app.RouteOptimizer.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Integer> {
}
