package com.app.pictolike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebView;
import android.webkit.WebViewClient;


	public class SettingsScreenActivity extends Activity implements OnItemClickListener{
		ListView listView;
		WebView webView;
		
	     
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_settingsscreen);
	         
	        listView = (ListView) findViewById(R.id.listSettings);
	        listView.setOnItemClickListener(this);        
	    }
	 
	    /*
	     * Parameters:
	        adapter - The AdapterView where the click happened.
	        view - The view within the AdapterView that was clicked
	        position - The position of the view in the adapter.
	        id - The row id of the item that was clicked.
	     */
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	    	String name = String.valueOf(((TextView) view).getText());
	    	Log.i("divy",name);
	    	
	    if( name.equals("Terms of Use")){
	    		String url = "http://itshoofar.com";
		    	Intent i = new Intent(Intent.ACTION_VIEW);
		    	i.setData(Uri.parse(url));
		    	startActivity(i);
		    		Log.i("divy","i run");
	    	}
	    	
	    	
	    }
	    
	  
	}
