package model;

public class Subscribe {
	public String subId;
	public String planId;
	public String userId;
	public Integer duration;
	
	public Subscribe(String subId, String planId, String userId, Integer duration) {
		this.subId = subId;
		this.planId = planId;
		this.userId = userId;
		this.duration = duration;
	}
	
	public String getSubId() { return subId; }
	public String getPlanId() { return planId; }
	public String getUserId() { return userId; }
	public int getDuration() { return duration; }

}