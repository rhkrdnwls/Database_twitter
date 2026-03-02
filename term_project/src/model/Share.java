package model;

public class Share {
	public String shareId;
	public String postId;
	public String sharingId;
	public String sharedTo;
	public String sharedAt;
	
	public Share(String shareId, String postId, String sharingId, String sharedTo, String sharedAt) {
		this.shareId = shareId;
		this.postId = postId;
		this.sharingId = sharingId;
		this.sharedTo = sharedTo;
		this.sharedAt = sharedAt;
	}
	
	public String getShareId() { return shareId; }
	public String getPostId() { return postId; }
	public String getSharingId() { return sharingId; }
	public String getSharedTo() { return sharedTo; }
	public String getSharedAt() { return sharedAt; }
	
}