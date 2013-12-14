package de.app.infoapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import de.app.infoapp.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;



public class RefreshService extends Service {
	
	
	 
	private  ArrayList<Termin>  mTermineNew= new ArrayList<Termin>();
	private ArrayList<String> mInfoText = new ArrayList<String>();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		getNews();
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private   void getNews (){
		if(isOnline()){
			Thread networkTheardTermine = new Thread(new Runnable(){
				@Override
				public void run(){
					checkDir();
					if(isOnline()){
						ArrayList<Groupe> mGroups = JGApplication.getmGroupeArrayList();
						mTermineNew.clear();
						//ändern
						
						for ( Groupe groupe:mGroups){
							if(groupe.isChecked()){
								downloadInfoText(groupe);
								downloadTermine(groupe);
							}
						}
						JGApplication.setmInfoText(mInfoText);
						storeInfoText();
						JGApplication.setmInfoText(mInfoText);

						//System.out.println(mTermineNew.get(0).toString());
						syncTermine();
						
					
						Intent mIntentAlarms = new Intent(getApplicationContext(), AlarmsService.class);
						startService(mIntentAlarms);
							//bindService(mIntentAlarms, JGApplication.getAlarmsServiceConnection(),BIND_AUTO_CREATE);
						
						//JGApplication.getmAlarmsService().exchange();
							
							
						
						
						storeTermine();
						Intent mintent = new Intent(getResources().getString(R.string.broadcast_intent_downloadfinished));
						LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mintent);
						//System.out.println("fertig");
						
					    String root = Environment.getExternalStorageDirectory().toString();
					    File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));    
					    String fname = getResources().getString(R.string.storage_filename_losungen);
					    File file = new File (LosungenDir, fname);
						SharedPreferences SharPref = getSharedPreferences(JGApplication.KEY_LOSUNGEN_FERTIG,0);
						boolean isLosungenFertig=  SharPref.getBoolean(JGApplication.KEY_LOSUNGEN_FERTIG, false);

					    
					    if(!file.exists()||(!isLosungenFertig)){
					    	
					    	downloadLosungen(file);
					    	mintent = new Intent("LosungenFertig");
					    	LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mintent);
					    	
					    }
					    
					}
					JGApplication.RefreshCalled =true;
					//unbindService(JGApplication.getAlarmsServiceConnection());
					stopSelf();
				}
			});
			
			

	
			networkTheardTermine.start();
		}
		else{
			JGApplication.RefreshCalled=true;
			stopSelf();
			
		}
	}
	
	public String nextCustomURL(int i){
		String url = JGApplication.getmGroupeArrayList().get(i).getmURL();
		return url;
	}
	
	
	private void checkDir(){
	    String root = Environment.getExternalStorageDirectory().toString();
	    File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));  
	    if(!LosungenDir.exists()){
	    	LosungenDir.mkdirs();
	    	
	    }
	}
	
	
	private void downloadLosungen(File file){
		
			
{
		    	
		    	try{
		    		
		    		FileOutputStream fos = new FileOutputStream(file); 

		            downloadFile(getResources().getString(R.string.url_losungen)+ 
		            		Calendar.getInstance().get(Calendar.YEAR)+
		            		".xml"
		            		, fos); 

		            fos.close(); 
		            
		    		SharedPreferences NewsDoc = getSharedPreferences(JGApplication.KEY_LOSUNGEN_FERTIG,0);
		    		SharedPreferences.Editor preferencesEditorNewsDoc = NewsDoc.edit();
		    		preferencesEditorNewsDoc.putBoolean(JGApplication.KEY_LOSUNGEN_FERTIG, true);
		    		preferencesEditorNewsDoc.commit();

		            
		    	
		    		
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		    }
	}
	
    public static void downloadFile(String url_str, OutputStream os) 
            throws IllegalStateException, MalformedURLException, 
            ProtocolException, IOException { 

        URL url = new URL(url_str.replace(" ", "%20")); 

        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 

        conn.setRequestMethod("GET"); 

        conn.connect(); 

        int responseCode = conn.getResponseCode(); 

        if (responseCode == HttpURLConnection.HTTP_OK) { 

            byte tmp_buffer[] = new byte[4096]; 

            InputStream is = conn.getInputStream(); 

            int n; 

            while ((n = is.read(tmp_buffer)) > 0) { 
                os.write(tmp_buffer, 0, n); 
                os.flush(); 
            } 

        } else { 
            throw new IllegalStateException("HTTP response: " + responseCode); 
        } 
    } 

	private void downloadInfoText(Groupe groupe){
		
		
		try{
			
			URL urlinfo = new URL(groupe.getmURL() +getResources().getString(R.string.url_filename_info)); 
			
			InputStreamReader isrinfo =new InputStreamReader(urlinfo.openStream(),"ISO-8859-1");//
			BufferedReader bufferinfo = new BufferedReader(isrinfo);
			
			String line = groupe.getmName()+"QWYD" + bufferinfo.readLine();
			//System.out.println(line);
			String nextline;
			while ((nextline = bufferinfo.readLine()) != null){
				line = line+ "\r\n" + nextline;
			}
			mInfoText.add(line);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
    
    
	private void downloadTermine(Groupe groupe){
		
	
		try {
			String line;
			
			Calendar datum, vorVierStunden = Calendar.getInstance();
			vorVierStunden.add(Calendar.HOUR, -4 );
			
			SharedPreferences sharPref= getSharedPreferences(JGApplication.WHICHDATENOTIFY, Context.MODE_PRIVATE);
			
			URL urltermin = new URL(groupe.getmURL() + getResources().getString(R.string.url_filename_termine)); //http://192.168.2.109/termine.txt
			InputStreamReader isrtermin =new InputStreamReader(urltermin.openStream(),"ISO-8859-1");
			BufferedReader buffertermin = new BufferedReader(isrtermin);
			
			String[] feldString;
			
			while((line = buffertermin.readLine())!= null){
				feldString= line.split("#");
				int Jahr =Integer.valueOf(feldString[3]),
						Monat =Integer.valueOf(feldString[2])-1,
						Tag=Integer.valueOf(feldString[1]),
						Stunden = Integer.valueOf(feldString[4]),
						Minuten=Integer.valueOf(feldString[5]);
				
				datum =Calendar.getInstance();
				datum.set(Jahr,// Jahr
						Monat,//Monat
						Tag,//Tag
						Stunden,//Stunden
						Minuten//Minuten
						
						);
				datum.set(Calendar.SECOND, 0);
				datum.set(Calendar.MILLISECOND, 0);
				//System.out.println(feldString[0]);
				
				
				if (datum.isLenient()){
					
					Termin termin = new Termin(datum,
							feldString[6],//Event/Prediger
							feldString[7],//Tagesleitung
							feldString[8],//Musik
							feldString[9],//Essen
							feldString[10],// Zusatzinfo
							sharPref.getBoolean(JGApplication.WHICHDATENOTIFY, true),
							sharPref.getString(getResources().getString(R.string.settings_key_noify_when), "20min"),
							Integer.parseInt(feldString[11])+10000000*groupe.getId()
							);
					//System.out.println(Integer.parseInt(feldString[11])+10000000*groupe.getId());
					if(termin.getDatum().after(vorVierStunden)){
						mTermineNew.add(termin);
						//System.out.println("Termin erstellt");
					}
					else{
						//System.out.println("Termin erstellt nicht");
						
					}
				}
			}

			/*start Test
			datum =Calendar.getInstance();
			datum.add(Calendar.HOUR, 1);
			//System.out.println(feldString[0]);
			
			//System.out.println("Termin erstellt214");
			if (datum.isLenient()){
				
				Termin termin = new Termin(datum,
						"Anlasssfjsdö",//Event/Prediger
						"der Pgfdapst",//Tagesleitung
						"paukesdn und Trompeten",//Musik
						"dosengfsdravioli",//Essen
						"gaasdant viel Text den ejh niemand liest" ,// Zusatzinfo
						true,
						 "1hour",
						59+10000000*groupe.getId()
						);
				
				if(termin.getDatum().after(vorVierStunden)){
					mTermineNew.add(termin);
					System.out.println("Termin erstellt");
				}
				else{
					System.out.println("Termin erstellt nicht");
					
				}
				// ende Test
			}*/
		
		}catch(Exception e){
			
			e.printStackTrace();
			stopSelf();
		}
		
	}
	/* mTermineNew ist die Liste aller neuen TErmine
	 * mtermineOld die alten  Termine
	 * 
	 * Man nehme sich der Reihe nach jeden altenTermin
	 * überprüfe die Liste mit den neuen Terminen ob der alte enthalten ist per ID
	 * 	wenn er enthalten ist setzt man isRemeber und NotificationWhen bei dem Termin aus der neuen Liste
	 * 	wenn nicht passiert nix
	 */
	private void syncTermine(){
		ArrayList<Termin> mtermineOld = JGApplication.getmTermine();
		int i= -1;
		for (Termin terOld:mtermineOld){
			if(((i=Termin.contains(mTermineNew,terOld))!=-1)){
				mTermineNew.get(i).setRemember(terOld.isRemember());
				mTermineNew.get(i).setNotificationWhen(terOld.getNotificationWhen());
				
			}

		}

		TermineSort(mTermineNew);
		JGApplication.setmTermine(mTermineNew);
		//System.out.println("Anzahl sync: "+mtermine.size());
		
	}

	@SuppressWarnings("unchecked")
	private void TermineSort(ArrayList<Termin> mtermine) {
		Collections.sort(mtermine);
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
	private boolean storeInfoText(){
		
		
		
		String fname = getResources().getString(R.string.storage_filename_infotext);
		String root = Environment.getExternalStorageDirectory().toString();
		File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));    
		File TermineFile = new File (LosungenDir, fname);
		
		
		try {
			if(!TermineFile.exists()){
				TermineFile.createNewFile();
			}
			FileOutputStream fileStream = new FileOutputStream(TermineFile);
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(mInfoText);
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
	
	private boolean isOnline(){
		
		ConnectivityManager CManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
	}

	
}