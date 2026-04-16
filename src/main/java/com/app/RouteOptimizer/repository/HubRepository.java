package com.app.RouteOptimizer.repository;

import com.app.RouteOptimizer.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub, Integer> {
    boolean existsByLocationCode(String locationCode);

    Optional<Hub> findByLocationCode(String locationCode);
}
