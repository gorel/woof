package edu.purdue.cs.woof;

/*
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.json.JSONObject;
*/

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import android.telephony.SmsManager;
import android.util.Log;

public class Barker {
	//Constants
	final int TIMEOUT_MILLISEC = 10000;
	final int TWEET_LENGTH = 139;
	
	//Send settings and stuff
	private Contact contact;
	private String message;
	private Twitter twitter;
	//private String modhash;
	
	public Barker(Contact contact, String message) {
		this.contact = contact;
		this.message = message;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false);
		cb.setOAuthConsumerKey("pobhlGyGJm57kvXL9KrYDGh3v");
		cb.setOAuthConsumerSecret("grIpV9QTuWAUvHJhZwjI8QLCmypes1KOGQ8ZXhm1m9x8l5U4XH");
		cb.setOAuthAccessToken("2878452934-hVylzmkScHKE82vlCc0avbxXzeckm6xU5R0Lver");
		cb.setOAuthAccessTokenSecret("v4gKjaLa4bo5iPBgQn9guEsqkqiZirVdsNWZgn0ENKElH");
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void bark() {
		sendSmsMessage();
		sendEmail();
		sendTweet();
		//sendRedditPM();
	}
	
	private void sendSmsMessage() {
		if (contact.getSmsNumber().length() > 0) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(contact.getSmsNumber(), null, message, null, null);
		}
	}
	
	private void sendEmail() {
		//user: youhaveawoof@gmail.com
		//pass: superwoofbot
		if (contact.getEmail().length() > 0) {
			(new Thread() {
				@Override
				public void run() {
					Log.i("SendMail", "Sending email to " + contact.getEmail());
					 try {   
		                 GmailSender sender = new GmailSender("youhaveawoof@gmail.com", "superwoofbot");
		                 sender.sendMail("You have a Woof!",   
		                         message,   
		                         "You.Have.A.Woof@gmail.com",   
		                         contact.getEmail());
		             } catch (Exception e) {   
		                 Log.e("SendMail", e.getMessage(), e);   
		             }
				}
			}).start();
		}
	}
	
	private void sendTweet() {
		if (contact.getTwitterHandle().length() > 0) {
			(new Thread() {
				public void run() {
					String handle;
					if (contact.getTwitterHandle().charAt(0) != '@') {
						handle = "@" + contact.getTwitterHandle() + " ";
					}
					else {
						handle = contact.getTwitterHandle() + " ";
					}

					int start = 0;
					while (start < message.length()) {
						String tweet;
						if (start + TWEET_LENGTH - handle.length() > message.length()) {
							tweet = handle + message.substring(start);
						}
						else {
							tweet = handle + message.substring(start, start + TWEET_LENGTH - handle.length());
						}
						start += TWEET_LENGTH;
						try {
							twitter.updateStatus(tweet);
							Log.i("Twitter", "Tweeting: " + tweet);
						}
						catch (TwitterException e) {
							Log.e("Twitter", e.getMessage(), e);
						}
					}
				}
			}).start();
		}
	}
	
	/*
	private void sendRedditPM() {
		//user: youhaveawoof
		//pass: superwoofbot
		//Can't use network on main thread
		(new Thread() {
			public void run() {
				loginToReddit();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
				HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
				HttpClient client = new DefaultHttpClient(httpParams);
						
				String jsonString = "{" + 
						"'api-type': 'json', " +
						"'to': '" + contact.getRedditUsername() + "', " +
						"'subject': 'You have a woof!', " + 
						"'text': '" + message.replaceAll("'", "\\'") + "', " +
						"'uh': '" + modhash + "'" + 
						"}";
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("task", "savemodel"));
				params.add(new BasicNameValuePair("code", jsonString));

				HttpPost request = new HttpPost("http://reddit.com/api/compose,json");
				
				try {
					request.setEntity(new UrlEncodedFormEntity(params));
					client.execute(request);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void loginToReddit() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("api-type", "json"));
		params.add(new BasicNameValuePair("user", "youhaveawoof"));
		params.add(new BasicNameValuePair("passwd", "superwoofbot"));
		params.add(new BasicNameValuePair("rem", "true"));

		HttpPost request = new HttpPost("http://reddit.com/api/compose.json");
		Scanner in = null;
		
		try {
			request.setEntity(new UrlEncodedFormEntity(params));
			Log.d("REDDIT", "entity is " + request.getEntity().toString());
			HttpResponse response = client.execute(request);
			in = new Scanner(response.getEntity().getContent());
			StringBuilder json = new StringBuilder();
			while (in.hasNextLine()) {
				json.append(in.nextLine());
			}
			
			JSONObject jsonObject = new JSONObject(json.toString());
			modhash = jsonObject.getString("modhash");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				in.close();
			}
		}
	}
	*/
}
