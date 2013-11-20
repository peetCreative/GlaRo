package de.app.infoapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import de.app.infoapp.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;



public class GroupesService extends Service {
	
	
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		getGroupes();
		return Service.START_NOT_STICKY;
	}


	private void getGroupes(){
		Thread thread = new Thread(new Runnable(){
	
			@Override
			public void run() {
				URL urlinfo;
				if(isOnline()){
					
				
				try {
					urlinfo = new URL(getResources().getString(R.string.url_URLseite_txt));
					InputStreamReader isrinfo =new InputStreamReader(urlinfo.openStream(),"ISO-8859-1");
					BufferedReader bufferinfo = new BufferedReader(isrinfo);
					String line;
					String[] lineParts;
					int i = 0;
					while((line = bufferinfo.readLine()) != null){
						i++;
						lineParts = line.split("URL");
						Groupe groupe = new Groupe(lineParts[0],lineParts[1], false, i);
						JGApplication.addGroupe(groupe);
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("fertig");
				Intent mintent = new Intent(getResources().getString(R.string.broadcast_intent_groupefinished));
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mintent);
			}
				stopSelf();
				
	
			}
			
		});
		thread.start();
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
