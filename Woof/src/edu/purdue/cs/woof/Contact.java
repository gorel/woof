package edu.purdue.cs.woof;

public class Contact {
	private String name;
	private String sms_number;
	private String email;
	private String twitter_handle;
	private String reddit_username;
	
	public Contact(String name, String sms_number, String email, String twitter_handle, String reddit_username) {
		this.name = name;
		this.sms_number = sms_number;
		this.email = email;
		this.twitter_handle = twitter_handle;
		this.reddit_username = reddit_username;
	}
	
	public String getName() {
		return name;
	}

	public String getSmsNumber() {
		return sms_number;
	}

	public String getEmail() {
		return email;
	}

	public String getTwitterHandle() {
		return twitter_handle;
	}

	public String getRedditUsername() {
		return reddit_username;
	}
}
