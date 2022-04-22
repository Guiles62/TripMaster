package tourGuide.model;

public class NearByAttractions {

    private Attraction attraction;

    private double distance;

    private int rewardsPoints;

    public NearByAttractions(Attraction attraction, double distance, int rewardsPoints) {
        this.attraction = attraction;
        this.distance = distance;
        this.rewardsPoints = rewardsPoints;
    }

    public NearByAttractions() {
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public void setRewardsPoints(int rewardsPoints) {
        this.rewardsPoints = rewardsPoints;
    }
}
