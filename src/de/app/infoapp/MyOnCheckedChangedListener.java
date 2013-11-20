package de.app.infoapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.ListView;



public class MyOnCheckedChangedListener implements android.widget.CompoundButton.OnCheckedChangeListener{

	ListView mGroupeListView;
	static int anzahl =0;
	private Context ctx;
	
	
	
	public MyOnCheckedChangedListener(ListView lv) {
		super();
		this.mGroupeListView = lv;

		//anzahl ++;
		//System.out.println("Listener: "+  MyOnCheckedChangedListener.anzahl);
	}




	@Override
	public void onCheckedChanged(CompoundButton CheckBox, boolean isChecked) {
		int pos = this.mGroupeListView.getPositionForView(CheckBox);
		 //System.out.println("Pos ["+pos+"]");
         if (pos != ListView.INVALID_POSITION) {
        	 JGApplication.setGroupeChecked(pos, isChecked);
     		
         }
	}
}
