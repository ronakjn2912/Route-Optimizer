package com.app.RouteOptimizer.repository;

import com.app.RouteOptimizer.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRepository extends JpaRepository<Hub, Integer> {
    boolean existsByLocationCode(String locationCode);
}
