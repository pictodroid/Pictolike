package com.app.pictolike;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends Activity {
	private WebView webView = null;
	private TextView tvBack=null;
	private TextView txtHeader=null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		tvBack=(TextView)findViewById(R.id.back);
		txtHeader=(TextView)findViewById(R.id.txtHeader);
		
		String strHeader=getIntent().getStringExtra("header");
		if(strHeader!=null && strHeader.length()>0)
		{
			txtHeader.setText(strHeader);
		}
		
		webView=(WebView)findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		startWebView("http://www.tutorialspoint.com");
				
		tvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	/**
	 * method used to start loading web view for corresponding url.
	 */
	private void startWebView(String url) {
		//Create new webview Client to show progress dialog when opening a url or click on link
		webView.setWebViewClient(new WebViewClient() {     

			//If you will not use this method url links are opeen in new brower not in webview
			public boolean shouldOverrideUrlLoading(WebView view, String url) {             
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			public void onPageFinished(WebView view, String url) {
			}
		});
		webView.loadUrl(url);
	}
}

