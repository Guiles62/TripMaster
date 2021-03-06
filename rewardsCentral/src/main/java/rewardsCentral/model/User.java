package rewardsCentral.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
	private UUID userId;
	private String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<Attraction> attractions = new ArrayList<>();
	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	private List<UserReward> userRewards = new ArrayList<>();



	public User() {
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	public void setVisitedLocations(List<VisitedLocation> visitedLocations) {
		this.visitedLocations = visitedLocations;
	}

	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public List<UserReward> getUserRewards() {
		return userRewards;
	}

	public void setUserRewards(List<UserReward> userRewards) {
		this.userRewards = userRewards;
	}

	public VisitedLocation getLastVisitedLocation() {
		if (visitedLocations.size()!=0){
			return visitedLocations.get(visitedLocations.size() - 1);
		}
		return null;
	}

	public List<Attraction> getAttractions() {
		return attractions;
	}

	public void setAttractions(List<Attraction> attractions) {
		this.attractions = attractions;
	}
}
