package com.app.RouteOptimizer.service;

import com.app.RouteOptimizer.dto.RouteRequest;
import com.app.RouteOptimizer.entity.Hub;
import com.app.RouteOptimizer.entity.Route;
import com.app.RouteOptimizer.exception.HubAlreadyExistsException;
import com.app.RouteOptimizer.exception.HubDoesNotExistsException;
import com.app.RouteOptimizer.repository.HubRepository;
import com.app.RouteOptimizer.repository.RouteRepository;
import com.app.RouteOptimizer.utils.RouteMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //activates mockito
public class LogisticsServiceTest {
    @Mock
    private HubRepository hubRepository;//connected with a mock of original repo

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private RouteMapper routeMapper;

    @InjectMocks
    private LogisticsService logisticsService;//our original service which will be injecting mocks instead of original objects

    @Test
    public void createHubTest(){//1. if hub is saved

        //Arrange
        Hub fakeHub = new Hub(1, "Mumbai", "MUM");
        // Stub the existence check → false (hub does not exist yet)
        when(hubRepository.existsByLocationCode("MUM")).thenReturn(false);

        when(hubRepository.save(fakeHub))
                .thenReturn(fakeHub);

        //Act
        Hub result = logisticsService.createHub(fakeHub);

        //Assert
        assertThat(result.getName()).isEqualTo("Mumbai");

        //Optional but important
        // Verify : To verify that the repository was actually called
        verify(hubRepository).save(fakeHub);

    }


    //2. Hub already exists
    @Test
    public void shouldThrowExceptionWhenHubAlreadyExists(){
        //Arrange
        Hub fakeHub = new Hub(1, "Mumbai", "Mum");
        when(hubRepository.existsByLocationCode(fakeHub.getLocationCode()))
                .thenReturn(true);

        //Act & Assert
        assertThrows(HubAlreadyExistsException.class,//expected type
                ()->logisticsService.createHub(fakeHub),//executed by
                "Hub Already Exists");//message

        //Verify
        verify(hubRepository).existsByLocationCode(fakeHub.getLocationCode());
        verify(hubRepository, never()).save(fakeHub);//crucial check -- making sure save isn't called once exception is triggered
    }

//    3. create route test
    @Test
    void createRouteTest(){

        //Arrange
        Hub sourceHub = new Hub(1, "Mumbai", "MUM");
        Hub destHub = new Hub(3, "Bihar", "BHR");
        RouteRequest fakeRouteRequest = new RouteRequest(1, sourceHub, destHub, 132.5, 6.0);

        //exception check stub
        when(hubRepository.existsById(fakeRouteRequest.getSourceHub().getId()))
                .thenReturn(true);//surpassed
        when(hubRepository.existsById(fakeRouteRequest.getDestinationHub().getId()))
                .thenReturn(true);//surpassed
        Route expectedEntity = new Route(1, sourceHub, destHub, 132.5, 6.0);//what mapper should produce
        Route savedRoute = new Route(1, sourceHub, destHub, 132.5, 6.0);//what mapper should produce

        //mapper stub -- stub here tells the service to stop and do what is needful
        when(routeMapper.toEntity(fakeRouteRequest))
                .thenReturn(expectedEntity);
        //save stub
        when(routeRepository.save(expectedEntity))
                .thenReturn(savedRoute);

        //Act
        Route result = logisticsService.createRoute(fakeRouteRequest);

        //Assert
        assertThat(savedRoute.getId()).isEqualTo(1);

        //Verify
        verify(hubRepository).existsById(1);
        verify(hubRepository).existsById(3);
        verify(routeMapper).toEntity(fakeRouteRequest);
        verify(routeRepository).save(expectedEntity);
    }

    //4. if hub does not exist for making a route
    @Test
    void hubDoesNotExistsFor_A_Route(){

        //Arrange
        Hub sourceHub = new Hub(1, "Chennai", "CHN");
        Hub destinationHub = new Hub(10, "Diu", "DI");
        RouteRequest fakeRequest = new RouteRequest(1, sourceHub, destinationHub, 12.5, 10.0);
        //existsById exception stub
        when(hubRepository.existsById(sourceHub.getId()))
                .thenReturn(true);
        when(hubRepository.existsById(destinationHub.getId()))
                .thenReturn(false);
        //Act & Assert
        assertThrows(HubDoesNotExistsException.class,
                () -> logisticsService.createRoute(fakeRequest),
                "Hub Does not Exists");

        //Verify
        verify(hubRepository).existsById(sourceHub.getId());
        verify(hubRepository).existsById(destinationHub.getId());
        verify(routeMapper, never()).toEntity(fakeRequest);//make sure doesn't get called
    }

    // 5. get all hubs
    @Test
    void getAllHubsTest(){
        //Arrange
        List<Hub> hubs = List.of(
                new Hub(1, "Mumbai", "Mum"),
                new Hub(2, "Pune", "Pun"),
                new Hub(3, "Hyderabad", "Hyd")
        );

        when(hubRepository.findAll())
                .thenReturn(hubs);

        //Act
        List<Hub> result = logisticsService.getHubs();

        //Assert
        assertThat(result).isEqualTo(hubs);

        //verify
        verify(hubRepository).findAll();
    }


}