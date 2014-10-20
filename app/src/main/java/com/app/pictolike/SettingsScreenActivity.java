package com.app.pictolike;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SettingsScreenActivity extends Fragment{
	private LinearLayout laySave=null;
	private LinearLayout layEmail=null;
	private LinearLayout layOpinion=null;
	private LinearLayout layTerms=null;
	private LinearLayout layPolicy=null;
	private LinearLayout layLogout=null;

    public void setSettingsListener(final SettingsListener pSettingsListener) {
        mSettingsListener = pSettingsListener;
    }

    public interface SettingsListener{
        void onSaveOriginalPhotos();
    }
    private SettingsListener mSettingsListener;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settingsscreen, container, false);
        setupViews(rootView);
        return rootView;
    }
    public void setupViews(View view){
        laySave=(LinearLayout)view.findViewById(R.id.saveLay);
        layEmail=(LinearLayout)view.findViewById(R.id.emailLay);
        layOpinion=(LinearLayout)view.findViewById(R.id.opinionLay);
        layTerms=(LinearLayout)view.findViewById(R.id.termLay);
        layPolicy=(LinearLayout)view.findViewById(R.id.policyLay);
        layLogout=(LinearLayout)view.findViewById(R.id.logoutLay);

        laySave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                  if (mSettingsListener!=null){
                      mSettingsListener.onSaveOriginalPhotos();
                  }
//                Intent i = new Intent(getActivity(),
//                        LandingScreenActivity.class);
//                startActivity(i);
            }
        });

        layEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(),
                        DialogEmailActivity.class);
                startActivity(i);
            }
        });

        layOpinion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(),
                        WebViewActivity.class);
                i.putExtra("header", "Your Opinion Matters");
                startActivity(i);
            }
        });

        layTerms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(),
                        WebViewActivity.class);
                i.putExtra("header", "Terms Of Use");
                startActivity(i);
            }
        });

        layPolicy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(),
                        WebViewActivity.class);
                i.putExtra("header", "Privacy Policy");
                startActivity(i);
            }
        });

        layLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(),
                        DialogLogoutActivity.class);
                startActivity(i);
            }
        });
    }
}
