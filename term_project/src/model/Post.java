package model;

public class Post {
	private String postId;
	private String text;
	private String id;
	private int like;
	private String dateStr;
	
	public Post(String postId, String text, String id, int like, String dateStr) {
		this.postId = postId;
		this.text = text;
		this.id = id;
		this.like = like;
		this.dateStr = dateStr;
	}
	
	public String getPostId() { return postId; }
	public String getText() { return text; }
	public String getId() { return id; }
	public Integer getLike() { return like; }
	public String getDateStr() { return dateStr; }

}