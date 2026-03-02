package model;

public class PostLike {
	private String likeId;
	private String postId;
	private String likerId;

	public PostLike(String likeId, String postId, String likerId) {
		this.likeId = likeId;
		this.postId = postId;
		this.likerId = likerId;
	}
	
	public String getlikeId() { return likeId; }
	public String getpostId() { return postId; }
	public String getLikerId() { return likerId; }
}