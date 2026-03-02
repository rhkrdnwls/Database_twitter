package model;

public class Comment {
	private String commentId;
	private String content; // text
	private String writerId; // wid
	private String postId; // pid
	private int numOfLikes; // like
	
	public Comment(String commentId, String content, String writerId, String postId, int numOfLikes) {
		this.commentId = commentId;
		this.content = content;
		this.writerId = writerId;
		this.postId = postId;
		this.numOfLikes = numOfLikes;
	}
	
	public String getCommentId() { return commentId; }
	public String getContent() { return content; }
	public String getWriterId() { return writerId; }
	public String getPostId() { return postId; }
	public int getNumOfLikes() { return numOfLikes; }
}