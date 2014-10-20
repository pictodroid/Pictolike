package com.app.pictolike;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class DialogLogoutActivity extends Activity {
	private EditText edYourSchoolStud = null;
	private EditText edAllSchoolStud = null;
	private TextView tvOk= null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dialog_logout);
		tvOk=(TextView)findViewById(R.id.ok);
		tvOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		edYourSchoolStud=(EditText)findViewById(R.id.edYourstud);
		edAllSchoolStud=(EditText)findViewById(R.id.edAllStud);
		edAllSchoolStud.setText("/2000");
		edAllSchoolStud.setSelection(0);

		/*edAllSchoolStud.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(!s.toString().contains("/2000")){
					edAllSchoolStud.setText("/2000");
					edAllSchoolStud.setSelection(0);
				}
			}
		});	*/
	}	
}
