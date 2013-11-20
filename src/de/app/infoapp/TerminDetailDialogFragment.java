package de.app.infoapp;

import java.util.ArrayList;
import java.util.Calendar;

import de.app.infoapp.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class TerminDetailDialogFragment extends DialogFragment {
	
	private Termin currentTermin;
	private int indexInList;
	private Spinner whennotifySpinner;
	private TextView wtagTV, datumTV,zeitTV,personeninfoTV,zusatzinfoTV;
	private CheckBox RememberCheckBox;


	public TerminDetailDialogFragment(Termin termin, int index) {
		super();
		currentTermin = termin;
		this.indexInList =index;
		//System.out.println("Termin ist"+termin.isRemember());
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater li = getActivity().getLayoutInflater();
			View itemView= li.inflate(R.layout.detaildialog, null);
			builder.setView(itemView);
			builder.setTitle(currentTermin.getAnlass());

			wtagTV = (TextView) itemView.findViewById(R.id.detaildialog_wtag_tv);
			datumTV = (TextView) itemView.findViewById(R.id.detaildialog_date_tv);
			zeitTV = (TextView) itemView.findViewById(R.id.detaildialog_time_tv);
			personeninfoTV = (TextView) itemView.findViewById(R.id.personalinformationTV);
			zusatzinfoTV = (TextView) itemView.findViewById(R.id.zusatzinfoTV);
			
			itemView.setBackgroundColor(currentTermin.getColorbgFormated(getActivity().getResources(), indexInList));

			wtagTV.setText(currentTermin.getWtagFormated(getActivity().getResources()));
			wtagTV.setTextColor(currentTermin.getColorWtagSchriftFormated(getActivity().getResources()));
			
			zeitTV.setText(currentTermin.getTimeFormated(getActivity().getResources()));
			
			datumTV.setText(currentTermin.getDatum().get(Calendar.DATE)+"."+
							(currentTermin.getDatum().get(Calendar.MONTH)+1)+"."+
							currentTermin.getDatum().get(Calendar.YEAR));
							
			

							


			personeninfoTV.setText(currentTermin.getPersoinfoFormated(getActivity().getResources(), 0));
			
			if(currentTermin.equals("-")){
				zusatzinfoTV.setText("");
			}else{
				zusatzinfoTV.setText(currentTermin.getZusatzinfo());
			}
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			boolean notifyActivate = sp.getBoolean(getResources().getString(R.string.settings_key_notify_is), false);
			
			RememberCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox_DetailDialog_Remember);
			whennotifySpinner=(Spinner) itemView.findViewById(R.id.detaildialog_whennotify_spinner); 
			
			
			
			
			
			if (notifyActivate){
				RememberCheckBox.setChecked(currentTermin.isRemember());
				RememberCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
						
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						currentTermin.setRemember(isChecked); 
						
						
					}
					
				});
				String[] a = getResources().getStringArray(R.array.listValues);
				int position = 0;
				for(int i = 0; i<a.length; i++){
					if(a[i].equals(currentTermin.getNotificationWhen())){
						position = i;
						break;
					}
				}

				whennotifySpinner.setSelection(position);
				whennotifySpinner.setOnItemSelectedListener(new whennotifyOnItemSelectedListener(getActivity(), currentTermin));

				
			}else{
				RememberCheckBox.setVisibility(View.INVISIBLE);
				whennotifySpinner.setVisibility(View.INVISIBLE);

				
			}
			
			
			
			
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
				public void onClick(DialogInterface dialog, int id) {
                	   
                	   
						Intent mIntentAlarms = new Intent(getActivity().getApplicationContext(), AlarmsService.class);
						getActivity().startService(mIntentAlarms);
                       
                   }
               });
		return builder.create();
	}
}
