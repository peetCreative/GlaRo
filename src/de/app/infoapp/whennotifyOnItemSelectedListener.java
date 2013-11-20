package de.app.infoapp;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class whennotifyOnItemSelectedListener implements OnItemSelectedListener {
	
	private Context ctx;
	private Termin termin;
	

	public whennotifyOnItemSelectedListener(Context ctx,Termin termin) {
		super();
		this.ctx = ctx;
		this.termin= termin;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		//System.out.println(ctx.getResources().getStringArray(R.array.listValues)[pos]);
		//System.out.println(pos);
		//JGApplication.setNotifyWhenTermin(termin,
				termin.setNotificationWhen( ctx.getResources().getStringArray(R.array.listValues)[pos]);
		

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		

	}

}
