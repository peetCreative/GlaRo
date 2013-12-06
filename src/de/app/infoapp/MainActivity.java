package de.app.infoapp;

import java.util.Locale;

import de.app.infoapp.R;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {



	/**
	* The {@link android.support.v4.view.PagerAdapter} that will provide
	* fragments for each of the sections. We use a
	* {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	* will keep every loaded fragment in memory. If this becomes too memory
	* intensive, it may be best to switch to a
	* {@link android.support.v4.app.FragmentStatePagerAdapter}.
	*/
	SectionsPagerAdapter mSectionsPagerAdapter;
	private BroadcastReceiver mBroadcastReceiver;
	ViewPager mViewPager;
	
	private  boolean mBoundedAlarmsService=false;
	private  AlarmsServiceConnection mAlarmsServiceConnection = new AlarmsServiceConnection();
	private  AlarmsService mAlarmsService;





	private  class AlarmsServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
				System.out.println("truejh");
			   mBoundedAlarmsService = true;
			   AlarmsService.MyBinder mMyBinder = (AlarmsService.MyBinder)service;
			   mAlarmsService = mMyBinder.getService();
			   System.out.println("ServiceConnection");
			   
		
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mAlarmsService = null;
			mBoundedAlarmsService = false;
			
		}
		
	}
	
	public AlarmsServiceConnection getAlarmsServiceConnection(){
		if(mAlarmsServiceConnection==null){
			mAlarmsServiceConnection = new AlarmsServiceConnection();
		}
		return mAlarmsServiceConnection;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		
		//setBackgroundResource(R.drawable.hintergrund1);
		
		
		
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		
		if(!JGApplication.RefreshCalled){
			Intent RefreshService =new Intent (this.getApplicationContext(),RefreshService.class);
			startService(RefreshService);
			
		}
		
		
		mBroadcastReceiver = new BroadcastLosungFertigReceiver();
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
		        mBroadcastReceiver, new IntentFilter("LosungenFertig"));
		
		boolean b = false;
		for (int i=0; b==false&&i<JGApplication.getmGroupeArrayList().size(); i++){
			b= JGApplication.getmGroupeArrayList().get(i).isChecked();
		}
		if(!b){
			SetupDialogFragment sdf = new SetupDialogFragment();
			sdf.show(getSupportFragmentManager(), "fjdsdfkdslfl");	
		}
	}

	private int extracted() {
		return R.color.color1;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean openSettings(MenuItem view){
		Intent settingsIntent = new Intent(this.getApplicationContext(),SettingsActivity.class );
		startActivity(settingsIntent);
		return true;
	
	}
	public boolean doRefresh(MenuItem view){
		Intent RefreshService =new Intent (this.getApplicationContext(),RefreshService.class);
		startService(RefreshService);
		return true;
	}
	
	public boolean nextInfo(MenuItem view){
		JGApplication.inkrementInfoTextNumber();
		if(mViewPager.getCurrentItem()==0){
			TerminSectionFragment adf =(TerminSectionFragment) mSectionsPagerAdapter.getItem(0);
			adf.populateTextView();
		}
		return true;
	}
	
	@Override
	public void onStop() {
	//System.out.println("onStop()");//gute Idee noch mal überlegen Settings auch gucken
	//if(!(JGApplication.getmTermine().containsAll(JGApplication.getmTermineNotification())&&
	//		JGApplication.getmTermineNotification().containsAll(JGApplication.getmTermine()))){
	//Intent AlarmsService =new Intent (this.getApplicationContext(),AlarmsService.class);
	//startService(AlarmsService);
	//}
		super.onStop();
		if(this.mBoundedAlarmsService){
			//System.out.println("mBoundTrue");
			unbindService(this.mAlarmsServiceConnection);
		}
		
	
	
	}
	
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
		FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}
	
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
		FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	public void onTabReselected(ActionBar.Tab tab,
		FragmentTransaction fragmentTransaction) {
	}
	
	private class BroadcastLosungFertigReceiver extends android.content.BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast toast = Toast.makeText(getApplicationContext(), "Losung fertig", Toast.LENGTH_LONG);
			 toast.show();
		
		}
	}
	
	
	
	
	/**
	* A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	* one of the sections/tabs/pages.
	*/
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
			switch(position){
				case 0:{
					fragment = new TerminSectionFragment();
					Bundle args = new Bundle();
					args.putInt(TerminSectionFragment.ARG_SECTION_NUMBER, position + 1);
					fragment.setArguments(args);
					return fragment;
				}
				case 1:{
					fragment = new LosungSectionFragment();
					Bundle args = new Bundle();
					args.putInt(LosungSectionFragment.ARG_SECTION_NUMBER, position + 1);
					fragment.setArguments(args);
					return fragment;
				}
				case 2:{
					fragment = new ChatroomSectionFragment();
					Bundle args = new Bundle();
					args.putInt(ChatroomSectionFragment.ARG_SECTION_NUMBER, position + 1);
					fragment.setArguments(args);
					return fragment;
				}
		
			}
			return null;
			
			
			
		}
		
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
		
			}
			return null;
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);


	}

/**
* A dummy fragment representing a section of the app, but that simply
* displays dummy text.
*/




}