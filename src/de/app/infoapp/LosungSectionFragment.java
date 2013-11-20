package de.app.infoapp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import de.app.infoapp.R;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;



public class LosungSectionFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	static private TextView LosungTV, LosungVersTV, LehrtextTV, LehrtextVersTV, HerrenhuterLink1TV, HerrenhuterLink2TV;
	//static private Button ChooseDateDialogButton;
	static private Calendar cal;

	private BroadcastReceiver mBroadcastReceiver;

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		LayoutInflater lf = getActivity().getLayoutInflater();
		View rootView = lf.inflate(R.layout.fragment_main_losung,null);
		cal = Calendar.getInstance(Locale.GERMANY);
		
		LosungTV = (TextView) rootView.findViewById(R.id.currentLosungContent);
		LosungVersTV = (TextView) rootView.findViewById(R.id.currentLosungVersContent);
		LehrtextTV = (TextView) rootView.findViewById(R.id.currentLehrtextContent);
		LehrtextVersTV = (TextView) rootView.findViewById(R.id.currentLehrtextVersContent);
		HerrenhuterLink1TV = (TextView) rootView.findViewById(R.id.fragment_main_losungen_herrenhuter1TV);
		HerrenhuterLink2TV = (TextView) rootView.findViewById(R.id.fragment_main_losungen_herrenhuter2TV);
		
	    Resources res = getResources();
	    int color_bg = res.getColor(R.color.app_bg);
	    rootView.setBackgroundColor(color_bg);

		try {
			fillAll();
		} 
		 catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParserConfigurationException e) {

		}
		fillLinks();
		
		mBroadcastReceiver = new BroadcastLosungFertigReceiver();
	    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(
	            mBroadcastReceiver, new IntentFilter("LosungenFertig"));

		
		return rootView;
	}

	private void fillLinks(){
		HerrenhuterLink1TV.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				String url = "http://www.ebu.de/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return false;
			}
			
		});
		HerrenhuterLink2TV.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				String url = "http://losungen.de/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return false;
			}
			
		});
		
	}
	private void fillAll() throws  IOException, ParserConfigurationException{
		// die Datei initalieisieren /Finden
		String 	losung,
				losungVers,
				lehrtext,
				lehrtextVers;
		
		String fname = getActivity().getResources().getString(R.string.storage_filename_losungen);
	    String root = Environment.getExternalStorageDirectory().toString();
	    File LosungenDir = new File(root + getActivity().getResources().getString(R.string.storage_folder));    
	  
	    File file = new File (LosungenDir, fname);
		
	    if(file.exists()){
	    	
	    
	 
		try{
		    SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
		    SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			FileReader reader = new FileReader(file);
			InputSource inputSource = new InputSource(reader);
		
		
	      LosungHandler lh = new LosungHandler(cal);
	      
	      xmlReader.setContentHandler(lh);
	      xmlReader.parse(inputSource);
	      losung = lh.getLosung().getLosungsText();
	      losungVers = lh.getLosung().getLosungsVers();
	      lehrtext = lh.getLosung().getLehrText();
	      lehrtextVers = lh.getLosung().getLehrtextVers();
	      
	      LosungTV.setText(formSpannable(losung), TextView.BufferType.SPANNABLE);
	      LosungVersTV.setText(losungVers);
	      LehrtextTV.setText(formSpannable(lehrtext), TextView.BufferType.SPANNABLE);
	      LehrtextVersTV.setText(lehrtextVers);

		} catch (SAXException e) {
			e.printStackTrace();
		}
	    }
	}
	
	private class BroadcastLosungFertigReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				fillAll();
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				
				e.printStackTrace();
			}

		}
	}




	public SpannableString formSpannable(String string){
		
		String string1 = string;
		String string2 = ""+string1;
		SpannableString str = null;
		
		String[] stringParts = string1.split("#");
		if(stringParts.length!=1){
			string1 = "";
			for(int i=0; i<stringParts.length;i++){
				string1 =string1+stringParts[i];
			}
			str = new SpannableString(string1);
			int start, end = 0;
			int anzahlStellen = (stringParts.length -1)/2;
			for (int i =0; i<anzahlStellen; i++){
				start =string2.indexOf("#", end+(i));
				end = string2.indexOf("#", start+1);
				
				str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
						start-(2*i),
						end-(2*i),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				
			}
			
		}
		
		stringParts = string1.split("/");
		if(stringParts.length!=1){
			string1 = "";
			for(int i=0; i<stringParts.length;i++){
				string1 =string1+stringParts[i];
			}
			str = new SpannableString(string1);

			int start, end = 0;
			int anzahlStellen = (stringParts.length -1)/2;
			for (int i =0; i<anzahlStellen; i++){
				start =string2.indexOf("/", end+(i));
				end = string2.indexOf("/", start+1);
				
				str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC),
						start-(2*i),
						end-(2*i),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				
			}
			
		}
		if (str==null){
			str = new SpannableString(string1);
		}

		
		return str;
		
	}


}

