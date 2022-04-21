package rewardsCentral.serviceUT;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rewardCentral.RewardCentral;
import rewardsCentral.model.Attraction;
import rewardsCentral.model.Location;
import rewardsCentral.model.User;
import rewardsCentral.model.VisitedLocation;
import rewardsCentral.service.Impl.RewardsCentralServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RewardsCentralServiceTest {

    private RewardsCentralServiceImpl rewardsCentralService;

    RewardCentral rewardCentral = mock(RewardCentral.class);

    User user = mock(User.class);

    private VisitedLocation visitedLocation;

    private Location location;

    Attraction attraction = mock(Attraction.class);

    @Before
    public void setup() {
        rewardsCentralService = new RewardsCentralServiceImpl();
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction("city","ccc","ccc",12.1,12.4));
        attractions.add(new Attraction("city","aaa","ccc",12.1,12.4));
        attractions.add(new Attraction("city","bbb","ccc",12.1,12.4));
        attractions.add(new Attraction("city","ddd","ccc",12.1,12.4));
        attractions.add(new Attraction("city","eee","ccc",12.1,12.4));
        UUID userId = UUID.fromString("c01f2ef7-27fb-4990-b1b2-e5e7f3690973");
        user = new User();
        user.setUserId(userId);
        user.setAttractions(attractions);
        location = new Location(1.22,32.00);
        visitedLocation = new VisitedLocation();
        visitedLocation.setLocation(location);
        visitedLocation.setTimeVisited(new Date());
        visitedLocation.setUserId(userId);
        visitedLocations.add(visitedLocation);
        user.setVisitedLocations(visitedLocations);
    }

    @Test
    public void getUserRewardsTest() {
        assertEquals(rewardsCentralService.getUserRewards(user),user.getUserRewards());
    }

    @Test
    public void getAttractionsRewardPointsTest() {
        assertTrue(rewardsCentralService.getAttractionRewardPoints(attraction.attractionId,user.getUserId()) > 0 );
    }
}
