package de.app.infoapp;

import de.app.infoapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class ChatroomSectionFragment extends Fragment {
	WebView mWebView;
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_main_chatroom,
				container, false);
		WebView mwebview = (WebView) rootView.findViewById(R.id.webview1);
		mwebview.getSettings().setJavaScriptEnabled(true);
		String url_shoutbox =getActivity().getResources().getString(R.string.url_chatbox);
		mwebview.loadUrl(url_shoutbox);
		return rootView;
	}

	public WebView getWebView() {
		return mWebView;
	}


	

}
