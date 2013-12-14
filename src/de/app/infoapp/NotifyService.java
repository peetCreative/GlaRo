package de.app.infoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class NotifyService extends Service{
	   public class ServiceBinder extends Binder {
	        NotifyService getService() {
	        	
	            return NotifyService.this;
	        }
	    }
	 
	    // Unique id to identify the notification.
	    private static final int NOTIFICATION = 123;
	    // Name of an intent extra we can use to identify if this service was started to create a notification  
	    public static final String INTENT_NOTIFY = "mNotification Peter";
	    // The system notification manager
	    private NotificationManager mNM;
	    
	    Termin cTermin;
	 
	    @Override
	    public void onCreate() {
	        
	        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        
	    }
	 
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        Log.i("LocalService", "Received start id " + startId + ": " + intent);
	        System.out.println("notify erhalten");
	        cTermin = (Termin) intent.getExtras().getSerializable("termin");
	        // If this service was started by out AlarmTask intent then we want to show our notification
	        showNotification();
	            
	         
	        // We don't care if this service is stopped as we have already delivered our notification
	        return START_NOT_STICKY;
	    }
	 
	    @Override
	    public IBinder onBind(Intent intent) {
	    	
	    	return mBinder;
	    }
	 
	    // This is the object that receives interactions from clients
	    private final IBinder mBinder = new ServiceBinder();
	 
	    /**
	     * Creates a notification and shows it in the OS drag-down status bar
	     */
	    private void showNotification() {
	    	
	    	String title= "";
	    	
	        // This is the 'title' of the notification
	    	if(cTermin==null){
	    		title = "cTermin ist wieder null";
	    		
	    	}else{
	    		title = cTermin.getAnlass();
	    		
	    	}
	        // This is the icon to use on the notification
	        int icon = R.drawable.ic_launcher;
	        // This is the scrolling text of the notification
			String infoText = "";
			
			if (!(cTermin.getGesamtVerantwortlicher().equals("-")))
				infoText ="Tagesleitung: " +cTermin.getGesamtVerantwortlicher();
			
			if (!(cTermin.getMusikVeranwortlicher().equals("-")))
					infoText = infoText+ " Musik: "+cTermin.getMusikVeranwortlicher();

			if(!(cTermin.getEssenVerantworlicher().equals("-")))
						infoText = infoText +" Essen: "+cTermin.getEssenVerantworlicher();


	        CharSequence text = infoText;       
	        // What time to show on the notification
	        long time = System.currentTimeMillis();
	         
	        Notification notification = new Notification(icon, text, time);
	 
	        // The PendingIntent to launch our activity if the user selects this notification
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
	 
	        // Set the info for the views that show in the notification panel.
	        notification.setLatestEventInfo(this, title, text, contentIntent);
	 
	        // Clear the notification when it is pressed
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	         
	        // Send the notification to the system.
	        mNM.notify(NOTIFICATION, notification);
	         
	        // Stop the service when we are finished
	        stopSelf();
		}

}
