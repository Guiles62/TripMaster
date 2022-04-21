package tripPricer.serviceTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tripPricer.Provider;
import tripPricer.TripPricer;
import tripPricer.model.Location;
import tripPricer.model.User;
import tripPricer.model.UserReward;
import tripPricer.model.VisitedLocation;
import tripPricer.service.Impl.TripPricerServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TripPricerServiceTest {

    TripPricer tripPricer = mock(TripPricer.class);

    User user = mock(User.class);

    private TripPricerServiceImpl tripPricerService;



    @Before
    public void setup() {
        tripPricerService = new TripPricerServiceImpl();
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        UUID userId = UUID.fromString("c01f2ef7-27fb-4990-b1b2-e5e7f3690973");
        user = new User();
        Location location = new Location(1.22,32.00);
        VisitedLocation visitedLocation = new VisitedLocation();
        visitedLocation.setLocation(location);
        visitedLocation.setTimeVisited(new Date());
        visitedLocation.setUserId(userId);
        visitedLocations.add(visitedLocation);
        user.setVisitedLocations(visitedLocations);
    }

    @Test
    public void getTripDealsTest() {
        List<tripPricer.Provider> providers  = new ArrayList<>();
        providers.add(new Provider(UUID.randomUUID(),"provider",100));
        Mockito.when(tripPricer.getPrice("apiKey",UUID.randomUUID(),1,1,1,100)).thenReturn(providers);
        assertEquals(tripPricerService.getTripDeals(user,"apiKey"), user.getTripDeals());
    }

    @Test
    public void getPriceTest() {
        List<tripPricer.Provider> providers  = new ArrayList<>();
        providers.add(new Provider(UUID.randomUUID(),"provider",100));
        Mockito.when(tripPricer.getPrice("apiKey",UUID.randomUUID(),1,1,1,100)).thenReturn(providers);
        assertTrue(tripPricerService.getPrice(user, "apiKey").size() == 5 );
    }
}
