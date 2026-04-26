package com.app.RouteOptimizer.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubDto {
    @NotBlank(message = "Name cannot be Empty")
    private String name;
    @Size(min = 3, max = 5, message = "Location code must be 3-5 characters")
    @Column(unique = true)
    private String locationCode;
}
