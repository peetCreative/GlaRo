package de.app.infoapp;

import java.util.Calendar;

import de.app.infoapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class TerminSectionFragment extends Fragment{

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
int color_bg,
	morgen_bg,
	morgen_schrift,
	heute_bg,
	heute_schrift,
	zeilenfarbe1,
	zeilenfarbe2;
static TextView NewsTV, GroupeTV ;
static ListView terminListe;


private BroadcastReceiver mMessageReceiver;

private class DownloadReadyBroadcastReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Toast toast = Toast.makeText(getActivity().getApplicationContext(), "fertig geladen", Toast.LENGTH_LONG);
		 toast.show();


		 populateTextView();
		 populateListView();
	}
};




	public static final String ARG_SECTION_NUMBER = "section_number";




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater lf = getActivity().getLayoutInflater();   
		View rootView = lf.inflate(R.layout.fragment_main_termin,container,false);
		mMessageReceiver = new DownloadReadyBroadcastReceiver();
		NewsTV = (TextView) rootView.findViewById(R.id.NewsTV);
		GroupeTV = (TextView) rootView.findViewById(R.id.fragment_main_groupename);
		terminListe = (ListView) rootView.findViewById(R.id.terminLV);
		
	    LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(
	            mMessageReceiver, new IntentFilter(getActivity().getResources().getString(R.string.broadcast_intent_downloadfinished)));

	    Resources res = getResources();
	    color_bg = res.getColor(R.color.app_bg);
	    morgen_bg = res.getColor(R.color.termine_morgen_bg);
	    morgen_schrift = res.getColor(R.color.termine_morgen_schrift);
	    heute_bg = res.getColor(R.color.termine_heute_bg);
	    heute_schrift = res.getColor(R.color.termine_heute_schrift);
	    zeilenfarbe1 = res.getColor(R.color.termine_zeilenfarbe1);
	    zeilenfarbe2 = res.getColor(R.color.termine_zeilenfarbe2);
	    
	    
	    //rootView.setBackgroundColor(color_bg);
	    

	    


	    
	    
		
		populateTextView();
	    populateListView();
	    
	    

    
		return rootView;
	}






	public void populateTextView() {
		
		
		//SharedPreferences infoDoc = getActivity().getSharedPreferences(JGApplication.FILENAMEINFO, 0);
		//String infoDocContent = infoDoc.getString(JGApplication.FILENAMEINFO, "nicht gesetzt") ;
		
		if(!JGApplication.getmInfoText().isEmpty()){
			String[] Info = JGApplication.getmInfoText().get(JGApplication.getInfoTextNumber()).split("QWYD");
			NewsTV.setText(Info[1]);
			GroupeTV.setText(Info[0]);
		}
	}

	private void populateListView() {
		ArrayAdapter<Termin> adapter ;
		adapter= new TerminArrayAdapter();
		//System.out.println("Anzahl Termine: "+JGApplication.getmTermine().size());
		terminListe.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				//System.out.println(""+ arg2);
				TerminDetailDialogFragment tdtf =
						new TerminDetailDialogFragment(JGApplication.getmTermine().get(arg2),arg2);
				tdtf.show(getFragmentManager(), "detailgh");
			}

			
		});
		terminListe.setAdapter(adapter);
		////System.out.println("populateListView" );
		////System.out.println(""+ Termine.size() );
	}


	class TerminArrayAdapter extends ArrayAdapter<Termin>{

		
		public TerminArrayAdapter() {
			super(getActivity(), R.layout.terminzeile, JGApplication.getmTermine());
			
			
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null){
				itemView =getActivity().getLayoutInflater().inflate(R.layout.terminzeile, parent, false);
				
			}
			
			
			Termin termin = JGApplication.getmTermine().get(position);
			
			TextView wtagTV = (TextView) itemView.findViewById(R.id.item_wochentag);
			TextView datumTV = (TextView) itemView.findViewById(R.id.item_datum);
			TextView zeitTV = (TextView) itemView.findViewById(R.id.item_zeit);
			TextView persinfoTV = (TextView) itemView.findViewById(R.id.item_Tagesleitung);

			itemView.setBackgroundColor(termin.getColorbgFormated(getActivity().getResources(), position));
			

			wtagTV.setText(termin.getWtagFormated(getActivity().getResources()));
			wtagTV.setTextColor(termin.getColorWtagSchriftFormated(getActivity().getResources()));
			
			zeitTV.setText(termin.getTimeFormated(getActivity().getResources()));
			
			datumTV.setText(termin.getDatum().get(Calendar.DATE)+"."+
							(termin.getDatum().get(Calendar.MONTH)+1)+"."+
							termin.getDatum().get(Calendar.YEAR));
							

			TextView eventTV = (TextView) itemView.findViewById(R.id.item_event);
			eventTV.setText(termin.getAnlass());
			


			persinfoTV.setText(termin.getPersoinfoFormated(getActivity().getResources(), 1));

			
			return itemView;
		}
		
	}


}