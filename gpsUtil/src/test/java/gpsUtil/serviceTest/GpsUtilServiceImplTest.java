package gpsUtil.serviceTest;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.model.Location;
import gpsUtil.model.User;
import gpsUtil.model.VisitedLocation;
import gpsUtil.service.Impl.GpsUtilServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GpsUtilServiceImplTest {

    private GpsUtilServiceImpl gpsUtilService;


    GpsUtil gpsUtil = mock(GpsUtil.class);


    User user = mock(User.class);


    private VisitedLocation visitedLocation;


    private Location location;


    @Before
    public void setup() {
        gpsUtilService = new GpsUtilServiceImpl(gpsUtil);
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        UUID userId = UUID.fromString("c01f2ef7-27fb-4990-b1b2-e5e7f3690973");
        user = new User(userId);
        location = new Location(1.22,32.00);
        visitedLocation = new VisitedLocation();
        visitedLocation.setLocation(location);
        visitedLocation.setTimeVisited(new Date());
        visitedLocation.setUserId(userId);
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
        gpsUtil.location.VisitedLocation userVisitedLocation = new gpsUtil.location.VisitedLocation(
                user.getUserId(),
                new gpsUtil.location.Location(1.20,1.10),
                new Date());
        Mockito.when(gpsUtil.getUserLocation(user.getUserId())).thenReturn(userVisitedLocation);
        assertTrue(gpsUtilService.trackUserLocation(user) == user.getLastVisitedLocation());
    }

    @Test
    public void trackAllUserLocationTest() {
        List<User> users = new ArrayList<>();
        users.add(user);
        gpsUtil.location.VisitedLocation userVisitedLocation = new gpsUtil.location.VisitedLocation(
                user.getUserId(),
                new gpsUtil.location.Location(1.20,1.10),
                new Date());
        Mockito.when(gpsUtil.getUserLocation(user.getUserId())).thenReturn(userVisitedLocation);
        assertTrue(gpsUtilService.trackAllUserLocation(users).get(0).getVisitedLocations() == user.getVisitedLocations());
    }

    @Test
    public void getNearByAttractionsTest() {
        List<gpsUtil.location.Attraction> attractions = new ArrayList<gpsUtil.location.Attraction>();
        attractions.add(new Attraction("city","ccc","ccc",12.1,12.4));
        attractions.add(new Attraction("city","aaa","ccc",12.1,12.4));
        attractions.add(new Attraction("city","bbb","ccc",12.1,12.4));
        attractions.add(new Attraction("city","ddd","ccc",12.1,12.4));
        attractions.add(new Attraction("city","eee","ccc",12.1,12.4));
        Mockito.when(gpsUtil.getAttractions()).thenReturn(attractions);
        assertEquals(5, gpsUtilService.getNearByAttractions(user).size());
    }

    @Test
    public void getAllAttractionsTest() {
        List<gpsUtil.location.Attraction> attractions = new ArrayList<gpsUtil.location.Attraction>();
        attractions.add(new Attraction("city","ccc","ccc",12.1,12.4));
        Mockito.when(gpsUtil.getAttractions()).thenReturn(attractions);
        assertEquals(1,gpsUtilService.getAllAttractions().size());
    }
}
