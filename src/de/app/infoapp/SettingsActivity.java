package de.app.infoapp;

import de.app.infoapp.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


@SuppressLint("ValidFragment")
public class SettingsActivity extends PreferenceActivity {

	private SettingsFragment mSettingsFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mSettingsFragment = new SettingsFragment();
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, mSettingsFragment)		
		.commit();
		 //PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.settings, false);
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onStop() {
		// gute Idee auch in Main gucken
//		if(!(JGApplication.getmTermine().containsAll(JGApplication.getmTermineNotification())&&
//				JGApplication.getmTermineNotification().containsAll(JGApplication.getmTermine()))){
		//Intent AlarmsService =new Intent (this.getApplicationContext(),AlarmsService.class);
		//startService(AlarmsService);
//		}
		

		super.onStop();
		//if(!JGApplication.ismBoundedAlarmsService()){
		//	unbindService(JGApplication.mAlarmsServiceConnection);
		//}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    onBackPressed();
	    return true;
	}

	public class SettingsFragment extends PreferenceFragment 
	implements OnSharedPreferenceChangeListener,
	OnPreferenceClickListener{


		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			////System.out.println("onChanged");
			if(key.equals( getResources().getString(R.string.settings_key_notify_is) )){
					JGApplication.setRememberAll(sharedPreferences.getBoolean(key, false));
					//System.out.println("which date notify"+sharedPreferences.getBoolean(key, false));
					
			}
			if(key.equals( getResources().getString(R.string.settings_key_noify_when) )){
				JGApplication.setNotifyWhen(sharedPreferences.getString(key, "20min"));
				populateNotifyWhenSummary();

					
			}
		}
			
		public void populateNotifyWhenSummary(){
			
			Preference sharedPref = findPreference(getResources().getString(R.string.settings_key_noify_when));
			String when= sharedPref.getSharedPreferences().getString(sharedPref.getKey(), "100");
			String summary= "";
			findPreference(getResources().getString(R.string.settings_key_noify_when)).setSummary(summary);
			if (when.equals("0min")){
				summary = (String) getText(R.string.settings_termin_whennotify_nullmin);
			}
			if (when.equals("20min")){
				summary = (String) getText(R.string.settings_termin_whennotify_zwanzigmin);
			}
			if (when.equals("1hour")){
				summary = (String) getText(R.string.settings_termin_whennotify_einsh);
			}
			if (when.equals("3hour")){
				summary = (String) getText(R.string.settings_termin_whennotify_dreih);
			}
			if (when.equals("6am")){
				summary = (String) getText(R.string.settings_termin_whennotify_sechsam);
			}
			if (when.equals("8am")){
				summary = (String) getText(R.string.settings_termin_whennotify_achtam);
			}
			////System.out.println("when date notify inflate" + when+"jsfd");
			summary= getText(R.string.settings_termin_whennotify_summarya)+" "+
					summary+" "+
					getText(R.string.settings_termin_whennotify_summaryb);
			findPreference(getResources().getString(R.string.settings_key_noify_when)).setSummary(summary);

		}
		
		@Override
		public void onResume() {
			
			super.onResume();
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		}

		 
		
		@Override
		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences().
				unregisterOnSharedPreferenceChangeListener(this);
			
			
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
				View view = super.onCreateView(inflater, container, savedInstanceState);
			   view.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
			   
			   

			   addPreferencesFromResource(R.xml.settings);
			   populateNotifyWhenSummary();
			   getPreferenceScreen().setOnPreferenceClickListener(this);

			   
			return view;
		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			////System.out.println("onClick");
			Intent intent = new Intent(getActivity(), GroupePreferenceActivity.class);
			startActivity(intent);
			return false;
		}

	}
}
