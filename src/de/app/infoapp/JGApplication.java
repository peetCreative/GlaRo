package de.app.infoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;

import de.app.infoapp.R;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;




public class JGApplication extends Application {
	
	
	
		private static ArrayList<Termin> mTermine= new ArrayList<Termin>();
		private static ArrayList<Groupe> mGroupeArrayList = new ArrayList<Groupe>();
		private static ArrayList<String> mInfoText = new ArrayList<String>();
		
		public static final String  FILENAMEINFO= "info";
		public static final String KEY_LOSUNGEN_FERTIG = "losfert";
		public static final String JGCODE = "pref_key_jgcode";
		public static final String WHICHDATENOTIFY = "notify";
		public static boolean RefreshCalled = false;
		
		private static int InfoTextNumber = 0;
		
		public static int expand = 3;



		
		



		
	public static void setGroupeChecked(int pos, boolean isChecked){
		mGroupeArrayList.get(pos).setChecked(isChecked);

	}

		
	public static ArrayList<Groupe> getmGroupeArrayList() {
			return mGroupeArrayList;
	}

	public static void setmGroupeArrayList(ArrayList<Groupe> mGroupArrayList) {
		JGApplication.mGroupeArrayList = mGroupArrayList;
	}
	
	public static void addGroupe(Groupe groupe){
		if(!mGroupeArrayList.contains(groupe)){
			mGroupeArrayList.add(groupe);
			////System.out.println("add");
		}
	}

	@Override
	public void onCreate() {
		
		////System.out.println("jfdasfkljgapp");
		
		getmTermineFromStorage();
		getmGroupeFromStorage();
		getmInfoTextFromStorage();
		deleteOutdatedTermine();
		
		//SharedPreferences doc = getSharedPreferences()
		

		RefreshCalled= false;
		
		
		
		super.onCreate();
	}



	public void deleteOutdatedTermine(){
		ArrayList<Termin> termineRemove= new ArrayList<Termin>();
		ArrayList<Termin> mtermine = JGApplication.getmTermine();
		Calendar DreiStunden =Calendar.getInstance();
		DreiStunden.roll(Calendar.HOUR_OF_DAY, -3);
		for (Termin termin:mtermine){
			if(termin.getDatum().before(DreiStunden)){
				termineRemove.add(termin);
			}
		}
		for(Termin terminRemove : termineRemove){
			mtermine.remove(terminRemove);
		}

	}
	

	public static ArrayList<Termin> getmTermine() {
		return mTermine;
	}
	
	public static void setmTermine(ArrayList<Termin> mtermine){
		mTermine= mtermine;
	}
	
	public static void setRememberAll(boolean b){
		for(Termin t: mTermine){
			t.setRemember(b);
		}
	}
	public static void setRemember(int i, boolean b){
		mTermine.get(i).setRemember(b);
	}
	public static void setNotifyWhen(String i){
		////System.out.println(i);
		for(Termin t: mTermine){
			t.setNotificationWhen(i);
		}
		
	}
	


	

	@SuppressWarnings("unchecked")
	private void getmInfoTextFromStorage() {
		String fname = getResources().getString(R.string.storage_filename_infotext);
	    String root = Environment.getExternalStorageDirectory().toString();
	    File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));    

	    File TermineFile = new File (LosungenDir, fname);

	    ArrayList<String> mtermine = new ArrayList<String>();
		    try {
			    if(TermineFile.exists()){
			    	
					FileInputStream fileStream = new FileInputStream(TermineFile);
					ObjectInputStream os = new ObjectInputStream(fileStream);
					
						mtermine =(ArrayList<String>) os.readObject();
						
					
					JGApplication.setmInfoText(mtermine);
					os.close();
			    }else{
			    	TermineFile.createNewFile();
			    }
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


	@SuppressWarnings("unchecked")
	private void getmTermineFromStorage() {
		String fname = getResources().getString(R.string.storage_filename_termine);
	    String root = Environment.getExternalStorageDirectory().toString();
	    File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));
	    //System.out.println(LosungenDir.exists());
	    //System.out.println(LosungenDir.canWrite());
	    //System.out.println(LosungenDir.canRead());
	    //System.out.println(root);

	    File TermineFile = new File (LosungenDir, fname);

	    ArrayList<Termin> mtermine = new ArrayList<Termin>();
		    try {
			    if(TermineFile.exists()){
			    	//System.out.println("zeige");
					FileInputStream fileStream = new FileInputStream(TermineFile);
					ObjectInputStream os = new ObjectInputStream(fileStream);
					
						mtermine = (ArrayList<Termin>) os.readObject();
						
					
					JGApplication.setmTermine(mtermine);
					os.close();
			    }else{
			    	TermineFile.createNewFile();
			    }
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@SuppressWarnings("unchecked")
	private void getmGroupeFromStorage() {
		String fname = getResources().getString(R.string.storage_filename_groupes);
		String root = Environment.getExternalStorageDirectory().toString();
		File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));    
		
		File TermineFile = new File (LosungenDir, fname);
		////System.out.println("getmafjdf");
		ArrayList<Groupe> mgroupes = new ArrayList<Groupe>();
		try {
			if(TermineFile.exists()){
				FileInputStream fileStream = new FileInputStream(TermineFile);
				ObjectInputStream os = new ObjectInputStream(fileStream);
				////System.out.println("getmafjdf23");
				
				mgroupes =(ArrayList<Groupe>) os.readObject();
				
				
				JGApplication.setmGroupeArrayList(mgroupes);
				////System.out.println("fdhsjk");
				////System.out.println("Anzahkl gruppen"+ getmGroupeArrayList().size());
				os.close();
			}else{
				TermineFile.createNewFile();
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	public static boolean checkExternalStorageAvaibility(){
		
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is ^+wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if(mExternalStorageAvailable && mExternalStorageWriteable){
			return true;
		}
		else{
			return false;
		}
	}


	public static ArrayList<String> getmInfoText() {
		return mInfoText;
	}


	public static void setmInfoText(ArrayList<String> mInfoText) {
		JGApplication.mInfoText = mInfoText;
	}


	public static int getInfoTextNumber() {
		return InfoTextNumber;
	}

	public static void setInfoTextNumber(int i){
		InfoTextNumber = i; 
	}
	
	public static void inkrementInfoTextNumber() {
		if(InfoTextNumber==mInfoText.size()-1)
			InfoTextNumber=0;
		else{
			if(InfoTextNumber<mInfoText.size())
				InfoTextNumber++;
		}
			
	}






	 
	 
	 

}
