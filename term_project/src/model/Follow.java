package model;

public class Follow {
	private String fId;
	private String followingId;
	private String followerId;
	
	public Follow(String fId, String followingId, String followerId) {
		this.fId = fId;
		this.followingId = followingId;
		this.followerId = followerId;
	}

	public String getfId() { return fId; }
	public String getFollowingId() { return followingId; }
	public String getFollowerId() { return followerId; }
	
}