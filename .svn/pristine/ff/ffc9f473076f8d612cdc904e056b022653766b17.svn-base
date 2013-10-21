package com.notnoop.mpns.sendnoti;

import com.notnoop.mpns.DeliveryClass;
import com.notnoop.mpns.MPNS;
import com.notnoop.mpns.MpnsNotification;
import com.notnoop.mpns.MpnsService;

public class SendToastNotification  {

	

	/*
	 * Uri is the Notification Channel
	 * contentOf10 means we only need the first 10 word of the content
	 * */
	
	private void pushShortNotification(String Uri, String userName,
			String messageID, String sender, String date, String contentOf34,String type
			,String title ) {
		
		MpnsService service=MPNS.newService().build();
		MpnsNotification notification;
		notification=MPNS.newNotification().toast().title(title).
				subtitle(date).parameter("/MainPage.xaml?title=" + title +
						"&name="+userName+
						"&type=" + type 
						+"&id=" + messageID +
						"&sender="+ sender +
						"&date=" + date +
						"&context=" + contentOf34).
						notificationClass(DeliveryClass.IMMEDIATELY).build();
		
		service.push(Uri,notification);
	}

	/*
	 * @param uri as the user's unique channel
	 * @param username as the user's name
	 * @param messageID the message's ID
	 * @param sender as who send the message
	 * @param date as the date when send the message
	 * @param contentOf10 as the 34 words of content
	 * @function send "news" to the client
	 * */
	public void pushShortNewsNotification(String Uri, String userName,
			String messageID, String sender, String date, String contentOf34
			,String title ) {
		
		pushShortNotification( Uri,  userName,
				 messageID,  sender,  date,  contentOf34, "news"
				, title );
	}
	
	
	/*
	 * @param uri as the user's unique channel
	 * @param username as the user's name
	 * @param messageID the message's ID
	 * @param sender as who send the message
	 * @param date as the date when send the message
	 * @param contentOf10 as the 34 words of content
	 * @function send "book" to the client
	 * */
	public void pushShortBookNotification(String Uri, String userName,
			String messageID, String sender, String date, String contentOf34
			,String title ) {
		
		pushShortNotification( Uri,  userName,
				 messageID,  sender,  date,  contentOf34, "book"
				, title );
	}
	
	/*
	 * @param uri as the user's unique channel
	 * @param username as the user's name
	 * @param messageID the message's ID
	 * @param sender as who send the message
	 * @param date as the date when send the message
	 * @param contentOf10 as the 34 words of content
	 * @function send "free" to the client
	 * */
	public void pushShortFreeNotification(String Uri, String userName,
			String messageID, String sender, String date, String contentOf34
			,String title ) {
		
		pushShortNotification( Uri,  userName,
				 messageID,  sender,  date,  contentOf34, "free"
				, title );
	}
}
