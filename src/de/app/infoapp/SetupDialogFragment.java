package de.app.infoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import de.app.infoapp.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;



public class SetupDialogFragment extends DialogFragment {
	private BroadcastReceiver mBroadcastReceiver;
	ListView GroupeListView;
	EditText UsernameEditText; 
	GroupeArrayAdapter adapter;

	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater li = getActivity().getLayoutInflater();
		View itemView= li.inflate(R.layout.dialog_main_setup, null);
		builder.setView(itemView);
		builder.setTitle(getActivity().getResources().getString(R.string.dialog_main_setup_title));
		
		GroupeListView = (ListView) itemView.findViewById(R.id.dialog_main_setup_main_groupsLV);
		UsernameEditText = (EditText) itemView.findViewById(R.id.dialog_main_setup_usernameET);
		
		
		
		adapter = new GroupeArrayAdapter(getActivity(), GroupeListView);
		GroupeListView.setAdapter(adapter);

		
		Intent RefreshService =new Intent (getActivity().getApplicationContext(),GroupesService.class);
		getActivity().startService(RefreshService);

		
		
		mBroadcastReceiver = new GroupesFinishedReceiver();
	    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(
	            mBroadcastReceiver, new IntentFilter(getActivity().getResources().getString(R.string.broadcast_intent_groupefinished)));

		
		
		
		
		
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
			public void onClick(DialogInterface dialog, int id) {
            	   storeGroupes();
                   
               }
           });

		
		return builder.create();
	}
	
	public void storeGroupes(){
		String fname = getResources().getString(R.string.storage_filename_groupes);
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
			/*
					for (Termin termin: JGApplication.getmTermine()){
						
						os.writeObject(termin);
					}*/
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

	private class GroupesFinishedReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			
			adapter = new GroupeArrayAdapter(getActivity(), GroupeListView);
			GroupeListView.setAdapter(adapter);



		}
	}




}
