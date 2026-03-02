package model;

public class Tag {
	public String tag_id;
	public String post_id;
	public String tagged_date;
	public String tagged_id;
	public String tagging_id;
	
	public Tag(String tag_id, String post_id, String tagged_date, String tagged_id, String tagging_id) {
		this.tag_id = tag_id;
		this.post_id = post_id;
		this.tagged_date = tagged_date;
		this.tagged_id = tagged_id;
		this.tagging_id = tagging_id;		
	}
	
	public String getTag_id() { return tag_id; }
	public String getPost_id() { return post_id; }
	public String getTagged_date() { return tagged_date; }
	public String getTagged_id() { return tagged_id; }
	public String getTagging_id() { return tagging_id; }
	
}