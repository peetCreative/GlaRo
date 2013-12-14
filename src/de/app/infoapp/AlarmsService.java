package de.app.infoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;


public class AlarmsService extends Service {
	
    // The android system alarm manager
	private final IBinder mBinder = new MyBinder();
    private AlarmManager am;
    private static ArrayList<Termin> gesetzteNotifications = new ArrayList<Termin>();
    // Your context to retrieve the alarm manager from
    

    public class MyBinder extends Binder{
    	public AlarmsService getService(){
    		return AlarmsService.this;
    	}
    }
    

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		System.out.println("alarmService");
		exchange();
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(gesetzteNotifications == null){
			getgesetzteNotificationsFromStorage();
		}
		return mBinder;
	}
	
	public void exchange(){
		Thread ExchangeThread =new Thread(new Runnable(){

			@Override
			public void run() {
				getgesetzteNotificationsFromStorage();
				//System.out.println("gesetzteNofications size" +gesetzteNotifications.size());
				ArrayList<Termin> zusetztenTermine = new ArrayList<Termin>();
				for(Termin termin:JGApplication.getmTermine()){
					if(termin.isRemember()){
						zusetztenTermine.add(termin);
					}
				}
				/*if(gesetzteNotifications.isEmpty()){
					System.out.println("is Empty");
					
				}else{
					System.out.println("is nicht Empty");
					
					//System.out.println(gesetzteNotifications.get(0).toString());
					System.out.println(gesetzteNotifications.size());
					
					
				}*/
				
				for(Termin gtermin:gesetzteNotifications){
					if(-1== Termin.contains(zusetztenTermine, gtermin)){
						removeTermin(gtermin);
						gesetzteNotifications.remove(gtermin);
					}
				}/*
				System.out.println("zusetztenTermine"+ zusetztenTermine.size());
				for(Termin mtermin:zusetztenTermine){
					System.out.println(mtermin.getAnlass());
					System.out.println(mtermin.getID());
					System.out.println(mtermin.getNotificationWhen());
					System.out.println(" ");
				}
				System.out.println("gesetzteTermine"+ gesetzteNotifications.size());
				for(Termin mtermin:gesetzteNotifications){
					System.out.println(mtermin.getAnlass());
					System.out.println(mtermin.getID());
					System.out.println(mtermin.getNotificationWhen());
					System.out.println(" " );
				}*/
				
				for(Termin mtermin:zusetztenTermine){
					System.out.println("überprüfe "+mtermin.getAnlass());
					if(-1 == Termin.containsTime(gesetzteNotifications,mtermin)){
						addTermin(mtermin);
						gesetzteNotifications.add(mtermin);
					}
				}
				storegesetzteNotifications();
				/*
				if(!zusetztenTermine.equals(gesetzteNotifications)){
					System.out.println("setzt Notification");
					removeAll();
					addAll();
					storegesetzteNotifications();
					
				}else{
					System.out.println("setze keine Notifications");

				}
				if(!gesetzteNotifications.isEmpty()){
					addTermin(gesetzteNotifications.get(0));
				}*/
				storeTermine();
				
				//Toast toast = Toast.makeText(getApplicationContext(), "Alarms fertig", Toast.LENGTH_LONG);
				// toast.show();
				 stopSelf();

				
				/*int i;
				for (Termin term: JGApplication.getmTermine()){
					if((i = Termin.containsTime(gesetzteNotifications, term) )== -1){
						
					}
				}*/
				
			}


			
		});
		ExchangeThread.start();
	}
	
	private boolean storeTermine(){
		
		

		String fname = getResources().getString(R.string.storage_filename_termine);
		String root = Environment.getExternalStorageDirectory().toString();
		File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));    
		File TermineFile = new File (LosungenDir, fname);
		
		
		try {
			if(!TermineFile.exists()){
				TermineFile.createNewFile();
			}
				FileOutputStream fileStream = new FileOutputStream(TermineFile);
				ObjectOutputStream os = new ObjectOutputStream(fileStream);
				os.writeObject(JGApplication.getmTermine());
				/*
				for (Termin termin: JGApplication.getmTermine()){
					
					os.writeObject(termin);
				}*/
				os.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			return false;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void removeAll() {
		
		if(!(this.gesetzteNotifications.isEmpty())){
			for (Termin termin : gesetzteNotifications){
				System.out.println("delete" + termin.toString());
				removeTermin(termin);
			}
			
		}
		gesetzteNotifications.clear();
	}

	private void addAll() {
		System.out.println("notify");
		for(Termin termin: JGApplication.getmTermine()){
			if(termin.isRemember()){
				System.out.println("add" + termin.toString());
				addTermin(termin);
				gesetzteNotifications.add(termin);
			}
			
		}
		
	}

	private void addTermin(Termin termin){
		
		Intent intent = new Intent(getApplicationContext(), NotifyService.class);
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
		intent.putExtra("termin", termin);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),termin.getID(), intent, 0);
		// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
		String[] a = getResources().getStringArray(R.array.listValues);
		int field = Calendar.MINUTE, verzug=0;
		Calendar alarmTermin= Calendar.getInstance();
				alarmTermin.setTimeInMillis(termin.getDatum().getTimeInMillis());
		if(termin.getNotificationWhen().equals(a[0])){
			verzug=0;
			field= Calendar.MINUTE;
		}
		if(termin.getNotificationWhen().equals(a[1])){
			verzug=-20;
			field= Calendar.MINUTE;
		}
		if(termin.getNotificationWhen().equals(a[2])){
			verzug=-1;
			field= Calendar.HOUR;
		}
		if(termin.getNotificationWhen().equals(a[3])){
			verzug=-3;
			field= Calendar.HOUR;
		}
		if(termin.getNotificationWhen().equals(a[4])){
			verzug=0;
			field= Calendar.MINUTE;
			alarmTermin.set(Calendar.HOUR, 6);
			alarmTermin.set(Calendar.MINUTE, 0);
		}
		if(termin.getNotificationWhen().equals(a[5])){
			verzug=0;
			field= Calendar.MINUTE;
			alarmTermin.set(Calendar.HOUR, 8);
			alarmTermin.set(Calendar.MINUTE, 0);
		}
			
		alarmTermin.roll(field, verzug);
		//alarmTermin.add(Calendar.MINUTE, -20);
		Calendar date2= Calendar.getInstance();
		date2.setTimeInMillis(alarmTermin.getTimeInMillis());
		am.set(AlarmManager.RTC, alarmTermin.getTimeInMillis(), pendingIntent);
		System.out.println("add " + termin.getAnlass()  + termin.getNotificationWhen()+" " +verzug +" "+  date2.get(Calendar.MINUTE) + " "+ termin.getTimeFormated(getApplicationContext().getResources()));
	}
	private void removeTermin(Termin termin){
		System.out.println("remove " +termin.getAnlass());
		Intent intent = new Intent(getApplicationContext(), NotifyService.class);
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
		intent.putExtra("termin", termin);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), termin.getID()																			, intent, 0);
		// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
		am.cancel(pendingIntent);
	}
/*
	private void setAlarms(){
		Thread AlarmTheard = new Thread(new Runnable(){

			@Override
			public void run() {
			    
				getgesetzteNotificationsFromStorage();
		        //System.out.println("AlarmTask1");
			    
			    ArrayList<Termin> terminDelete = new ArrayList<Termin>();
			    
			    Intent intent;
			    PendingIntent pendingIntent;
			    // löschen der abgewählten Alarms
			    for(Termin termin: gesetzteNotifications){
			    	if ((!aktuelleNotificationTermine.contains(termin))){
			    		// Request to start are service when the alarm date is upon us
			    		// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
			    		intent = new Intent(getApplicationContext(), NotifyService.class);
			    		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
			    		intent.putExtra("termin", termin);
			    		pendingIntent = PendingIntent.getService(getApplicationContext(), termin.getDatum().get(Calendar.YEAR)+
			    																			10000* termin.getDatum().get(Calendar.DAY_OF_MONTH)+
			    																			1000000* termin.getDatum().get(Calendar.MONTH)
			    																			, intent, 0);
			    		// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
			    		am.cancel(pendingIntent);
			    		terminDelete.add(termin);
			    		//System.out.println("gelöscht");
			    		
			    	}
			    }	
			    
			    //lösche alte Termine
			    for (Termin termindelete: terminDelete){
			    	gesetzteNotifications.remove(termindelete);
			    }
			    	
				//setze neu alarms
				for (Termin terminneu : aktuelleNotificationTermine){
					if((!gesetzteNotifications.contains(terminneu))&&
							terminneu.isRemember()){
						
						// Request to start are service when the alarm date is upon us
						// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
						intent = new Intent(getApplicationContext(), NotifyService.class);
						intent.putExtra(NotifyService.INTENT_NOTIFY, true);
						intent.putExtra("termin", terminneu);
						pendingIntent = PendingIntent.getService(getApplicationContext(),terminneu.getDatum().get(Calendar.YEAR)+
								10000* terminneu.getDatum().get(Calendar.DAY_OF_MONTH)+
								1000000* terminneu.getDatum().get(Calendar.MONTH), intent, 0);
						// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
						am.set(AlarmManager.RTC, terminneu.getDatum().getTimeInMillis(), pendingIntent);
						gesetzteNotifications.add(terminneu);
					}
				}
				storegesetzteNotifications();
			    //am.cancel(pendingIntent);
			    //System.out.println("AlarmTask2");
			    stopSelf();
			

				
				
			}
			
		});
		AlarmTheard.start();
	}
	
*/
private void getgesetzteNotificationsFromStorage() {
	String fname = getString(R.string.storage_filename_termine_notify);
    String root = Environment.getExternalStorageDirectory().toString();
    File LosungenDir = new File(root + getString(R.string.storage_folder));    
  
    File TermineFile = new File (LosungenDir, fname);

    
	    try {
		    if(TermineFile.exists()){
				FileInputStream fileStream = new FileInputStream(TermineFile);
				ObjectInputStream os = new ObjectInputStream(fileStream);
				this.gesetzteNotifications =	 (ArrayList<Termin>) os.readObject();
				////System.out.println(this.gesetzteNotifications==null);
				////System.out.println(this.gesetzteNotifications.isEmpty());
				os.close();
		    }else{
		    	TermineFile.createNewFile();
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

private boolean storegesetzteNotifications(){
	String fname =getString(R.string.storage_filename_termine_notify);
	String root = Environment.getExternalStorageDirectory().toString();
	File LosungenDir = new File(root + getString(R.string.storage_folder));    
	
	File TermineFile = new File (LosungenDir, fname);
	
	
	try {
		if(TermineFile.exists()){
			FileOutputStream fileStream = new FileOutputStream(TermineFile);
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(gesetzteNotifications);
			os.close();
		}else{
			TermineFile.createNewFile();
			FileOutputStream fileStream = new FileOutputStream(TermineFile);
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(gesetzteNotifications);
			os.close();
		}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	} catch (StreamCorruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	return true;
}
}
