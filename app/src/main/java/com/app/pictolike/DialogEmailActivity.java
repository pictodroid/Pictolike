package com.app.pictolike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DialogEmailActivity extends Activity {
	private TextView txtCancel = null;
	private TextView txtSet = null;
	private EditText edEmail = null;
	NumberPicker np = null;
	String[] schoolArr;
	ArrayList<SchoolInfo> schoolList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dialog_email);
		txtCancel = (TextView) findViewById(R.id.cancel);
		txtSet = (TextView) findViewById(R.id.set);
		edEmail=(EditText)findViewById(R.id.edMail);


        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();

            }
        });

        txtSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String email = edEmail.getText().toString();
                if(email!=null&& email.length()>4){
                    String temp = email.substring(email.length()-4,email.length());
                    if(temp.equalsIgnoreCase(".edu")){
                        showDialog("Thanks for the update, now please verify your email by checking your email!");
                    }else{
                        showDialog("Only .edu email are accepted at this time");
                    }
                }else{
                    showDialog("Invalid Email");
                }
            }
        });
		/*np = (NumberPicker) findViewById(R.id.npEmail);

		if (schoolList!=null && schoolList.size()>0) {
			schoolList.clear();
		}
		schoolList= parseXML();

		*//*String[] values = new String[9];
		values[0] = ".edu";
		values[1] = "@gmail.com";
		values[2] = "@yahoo.com";
		values[3] = "@hotmail.com";
		values[4] = "@outlook.com";
		values[5] = "@mail.com";
		values[6] = "@hushmail.com";
		values[7] = "@zoho.com";
		values[8] = "@yandex.com";*//*

		schoolArr = new String[schoolList.size()];
		int index = 0;

		for (SchoolInfo value : schoolList) {
			schoolArr[index] = value.schoolName;
			index++;
		}

		np.setMaxValue(schoolArr.length - 1);
		np.setMinValue(0);
		np.setDisplayedValues(schoolArr);

		np.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {

			}
		});

		txtCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		txtSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String strName=edEmail.getText().toString();
				if(strName!=null && strName.length()>0)
				{
					String strSchool= schoolArr[np.getValue()];
					Toast.makeText(DialogEmailActivity.this, strName+"@"+strSchool+".edu", Toast.LENGTH_LONG).show();

				}else{
					Toast.makeText(DialogEmailActivity.this, "Please Enter Name.", Toast.LENGTH_LONG).show();
				}
			}
		});*/
	}
private void showDialog(String message){

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
    AlertDialog alert = builder.create();
    alert.show();
}
	private ArrayList<SchoolInfo> parseXML() {
		AssetManager assetManager = getBaseContext().getAssets();
		try {
			InputStream is = assetManager.open("school_name_list.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			SchoolXmlHandler myXMLHandler = new SchoolXmlHandler();
			xr.setContentHandler(myXMLHandler);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);
			schoolList = myXMLHandler.getSchoolList();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return schoolList;
	}
}
