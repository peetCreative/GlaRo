package de.app.infoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import de.app.infoapp.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class GroupePreferenceActivity extends Activity {

	private BroadcastReceiver mBroadcastReceiver;
	private ListView GroupeListView;

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    onBackPressed();
	    return true;
	}
	
	public void storeGroupes(){
		String fname =getResources().getString(R.string.storage_filename_groupes);
		String root = Environment.getExternalStorageDirectory().toString();
		File LosungenDir = new File(root + getResources().getString(R.string.storage_folder));    
		File TermineFile = new File (LosungenDir, fname);
		
		
		try {
			if(!TermineFile.exists()){
				TermineFile.createNewFile();
			}
			FileOutputStream fileStream = new FileOutputStream(TermineFile);
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(JGApplication.getmGroupeArrayList());
			os.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	@Override
	public void onCreate(Bundle bundle) {
		
		setContentView(R.layout.dialog_settings_groupes);
		
		GroupeListView = (ListView) findViewById(R.id.dialog_settings_groupes_groupesLV);
		ArrayAdapter<Groupe> adapter = new GroupeArrayAdapter(this,  this.GroupeListView);
		GroupeListView.setAdapter(adapter);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	    super.onCreate(bundle);
		
	}

	public void onClick(View view){
		Intent intent = new Intent(this, GroupesService.class);
		startService(intent);
		
		mBroadcastReceiver = new GroupesFinishedReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mBroadcastReceiver, new IntentFilter(getResources().getString(R.string.broadcast_intent_groupefinished)));

	}
	
	
	
	
	
	
	
	@Override
	public void onStop() {
		storeGroupes();
		//if(!JGApplication.ismBoundedAlarmsService()){
		//	unbindService(JGApplication.mAlarmsServiceConnection);
		//}
		super.onStop();
	}




	

	private class GroupesFinishedReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//ArrayAdapter<Groupe> adapter = new GroupeArrayAdapter(, mGroupes);
			//GroupeListView.setAdapter(adapter);

		}
	}


	
}
