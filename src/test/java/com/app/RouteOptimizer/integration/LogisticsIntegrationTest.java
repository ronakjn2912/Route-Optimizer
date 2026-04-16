package com.app.RouteOptimizer.integration;

import com.app.RouteOptimizer.dto.RouteRequest;
import com.app.RouteOptimizer.entity.Hub;
import com.app.RouteOptimizer.entity.Route;
import com.app.RouteOptimizer.repository.HubRepository;
import com.app.RouteOptimizer.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test") //load application.properties of test
@AutoConfigureMockMvc
public class LogisticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;//for JSON conversion

    @Autowired
    private HubRepository hubRepository;// To clean/setup data

    @Autowired
    private RouteRepository routeRepository;

    @BeforeEach
    void setUp(){
        //clean database before each test
        routeRepository.deleteAll();
        hubRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"ADMIN"})
    void shouldCreateHubRouteAndFindShortestPath() throws Exception {
        // Step 1 : Create Hubs

        Hub hub1 = new Hub();
        hub1.setName("Mumbai");
        hub1.setLocationCode("Mum01");

        Hub hub2 = new Hub();
        hub2.setName("Pune");
        hub2.setLocationCode("Pun01");
        mockMvc.perform(post("/api/v1/hubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hub1)))
                        .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/hubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hub2)))
                .andExpect(status().isCreated());//will create hubs with unique ids

        // 2. Fetch real generated IDs using locationCode
        Hub hub1WithId = hubRepository.findByLocationCode(hub1.getLocationCode())
                                                .orElseThrow(() -> new RuntimeException("Hub Hyd01 not found"));

        Hub hub2WithId = hubRepository.findByLocationCode(hub2.getLocationCode())
                .orElseThrow(() -> new RuntimeException("Hub Pun01 not found"));

        //Step 2 : Create route between them

        RouteRequest routeRequest = new RouteRequest();
        routeRequest.setSourceHub(hub1WithId);
        routeRequest.setDestinationHub(hub2WithId);
        routeRequest.setDistance(250.0);
        routeRequest.setTime(3.0);

        mockMvc.perform(post("/api/v1/route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeRequest)))
                .andExpect(status().isCreated());


        //Step 3 : Test shortest path
        mockMvc.perform(get("/api/v1/shortest-path?sourceId={sourceId}&destinationId={destinationId}", hub1WithId.getId(), hub2WithId.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.HubIds").isArray())
                .andExpect(jsonPath("$.HubIds[0]").value(hub1WithId.getId()))
                .andExpect(jsonPath("$.HubIds[1]").value(hub2WithId.getId()))
                .andExpect(jsonPath("$.totalDistance").value(250.0))
                .andExpect(jsonPath("$.totalTime").value(3.0));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"ADMIN"})
    void shouldReturnBadRequest_whenSourceHubDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/shortest-path?sourceId=999&destinationId=1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"ADMIN"})
    void shouldReturnEmptyList_whenNoRouteExists() throws Exception {
        Hub hub1 = new Hub();
        hub1.setLocationCode("Mum01");
        hub1.setName("Mumbai");

        mockMvc.perform(post("/api/v1/hubs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hub1)))
                .andExpect(status().isCreated());

        Hub hub2 = new Hub();
        hub2.setLocationCode("Bhr01");
        hub2.setName("Bihar");

        mockMvc.perform(post("/api/v1/hubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hub2)))
                .andExpect(status().isCreated());

        Hub hub1WithId = hubRepository.findByLocationCode(hub1.getLocationCode())
                .orElseThrow(() -> new RuntimeException("Hub Hyd01 not found"));

        Hub hub2WithId = hubRepository.findByLocationCode(hub2.getLocationCode())
                .orElseThrow(() -> new RuntimeException("Hub Pun01 not found"));
        mockMvc.perform(get("/api/v1/shortest-path?sourceId={hub1WithId}&destinationId={hub2WithId}", hub1WithId.getId(), hub2WithId.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.HubIds").isEmpty())
                .andExpect(jsonPath("$.totalDistance").value(0))
                .andExpect(jsonPath("$.totalTime").value(0))
        ;

    }

}
