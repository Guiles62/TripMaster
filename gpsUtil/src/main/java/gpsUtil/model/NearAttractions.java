package gpsUtil.model;

import gpsUtil.location.Attraction;

public class NearAttractions {

    private Attraction attraction;
    private Double distanceInMilesBetweenUserAndAttraction;

    public NearAttractions(Attraction attraction, Double distanceInMilesBetweenUserAndAttraction) {
        this.attraction = attraction;
        this.distanceInMilesBetweenUserAndAttraction = distanceInMilesBetweenUserAndAttraction;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Double getDistanceInMilesBetweenUserAndAttraction() {
        return distanceInMilesBetweenUserAndAttraction;
    }

    public void setDistanceInMilesBetweenUserAndAttraction(Double distanceInMilesBetweenUserAndAttraction) {
        this.distanceInMilesBetweenUserAndAttraction = distanceInMilesBetweenUserAndAttraction;
    }
}
