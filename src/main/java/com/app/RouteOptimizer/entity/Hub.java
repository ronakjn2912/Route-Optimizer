package com.app.RouteOptimizer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//if provided int, then Jackson which serializes json object into java object assigns null to empty value, which throws exception as 'primitive' data types cannot be null.
    @NotBlank(message = "Name cannot be Empty")
    private String name;
    @Size(min = 3, max = 5, message = "Location code must be 3-5 characters")
    @Column(unique = true)
    private String locationCode;


}
