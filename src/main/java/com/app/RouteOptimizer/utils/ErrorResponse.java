package com.app.RouteOptimizer.utils;

import java.time.LocalDateTime;
//record - Spring Boot's default JSON library, Jackson, automatically serializes records into JSON responses.
public record ErrorResponse(LocalDateTime timestamp, String message, String details){}
