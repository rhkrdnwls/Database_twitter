package model;

public class CommentLike {
	private String clid;
	private String cmtid;
	private String liker_id;
	
	public CommentLike(String clid, String cmtid, String liker_id) {
		this.clid = clid;
		this.cmtid = cmtid;
		this.liker_id = liker_id;
	}
	
	public String getClid() { return clid; }
	public String getCmtid() { return cmtid; }
	public String getLiker_id() { return liker_id; }

}