package model;

public class User {
	public String user_id;
	public String pwd;
	public String phoneNumber;
	public String email;
	
	public User(String user_id, String pwd, String phoneNumber, String email) {
		this.user_id = user_id;
		this.pwd = pwd;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
	
	public String getUserId() { return user_id; }
	public String getPwd() { return pwd; }
	public String getPhoneNumber() { return phoneNumber; }
	public String getEmail() { return email; }


}