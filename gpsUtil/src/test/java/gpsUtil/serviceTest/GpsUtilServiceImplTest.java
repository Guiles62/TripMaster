package gpsUtil.serviceTest;

import gpsUtil.GpsUtil;
import gpsUtil.model.Attraction;
import gpsUtil.model.Location;
import gpsUtil.model.User;
import gpsUtil.model.VisitedLocation;
import gpsUtil.service.Impl.GpsUtilServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GpsUtilServiceImplTest {

    private GpsUtilServiceImpl gpsUtilService;

    @MockBean
    GpsUtil gpsUtil;
    @InjectMocks
    private User user;
    @InjectMocks
    private VisitedLocation visitedLocation;
    @InjectMocks
    private Location location;


    @Before
    public void setup() {
        gpsUtilService = new GpsUtilServiceImpl(gpsUtil);
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        visitedLocation = new VisitedLocation();
        visitedLocation.setLocation(location);
        UUID userId = UUID.fromString("c01f2ef7-27fb-4990-b1b2-e5e7f3690973");
        user = new User(userId);
        user.addToVisitedLocations(visitedLocation);
        visitedLocations.add(visitedLocation);

    }

    @Test
    public void getUserLocationTest() {
        gpsUtilService.getUserLocation(user);
        assertTrue(gpsUtilService.getUserLocation(user) == visitedLocation);
    }

    @Test
    public void trackUserLocationTest() {
        gpsUtilService.trackUserLocation(user);
        assertTrue(gpsUtilService.trackUserLocation(user) == visitedLocation);
    }

    @Test
    public void getNearByAttractionsTest() {
        gpsUtilService.getNearByAttractions(user);
        assertEquals(0, gpsUtilService.getNearByAttractions(user).size());
    }

    @Test
    public void getAllAttractionsTest() {
        gpsUtilService.getAllAttractions();
        assertEquals(0,gpsUtilService.getAllAttractions().size());
    }

}
