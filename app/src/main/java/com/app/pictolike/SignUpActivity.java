package com.app.pictolike;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pictolike.Utils.AppConfig;
import com.app.pictolike.mysql.MySQLCommand;
import com.app.pictolike.mysql.MySQLConnect;
import com.app.pictolike.mysql.SignupCommand;

import java.util.Calendar;
import java.util.Scanner;

public class SignUpActivity extends AbstractAppActivity implements MySQLCommand.OnCompleteListener {

    private static final int DATE_SELECTION_DIALOG = 0;

    private EditText edtUserName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtBirthday;
    private TextView txtTermsAndCondition;
    private RelativeLayout mTopLyt;
    private ImageView maleGenButton;
    private ImageView femaleGenButton;
    private String sel_gen = "";
    private Calendar mBirthDay;
    private int day;
    private int month;
    private int year;


    /* **************************************************************** */
    /* ******************* AbstractAppActivity ************************ */
    /* **************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();

        setContentView(R.layout.activty_signup);

        edtUserName = (EditText) findViewById(R.id.reg_email_edittext);
        edtEmail = (EditText) findViewById(R.id.reg_email_edittext);
        edtPassword = (EditText) findViewById(R.id.reg_password_edittext);
        edtBirthday = (EditText) findViewById(R.id.reg_birthday_edittext);
        txtTermsAndCondition=(TextView)findViewById(R.id.tv_terms_and_condition);
        txtTermsAndCondition.setMovementMethod(LinkMovementMethod.getInstance());
        mBirthDay = Calendar.getInstance();
        day = mBirthDay.get(Calendar.DAY_OF_MONTH);
        month = mBirthDay.get(Calendar.MONTH);
        year = mBirthDay.get(Calendar.YEAR);
        mTopLyt =(RelativeLayout) findViewById(R.id.lytmain);
        mTopLyt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getCurrentFocus()!=null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
        findViewById(R.id.reg_birthday_edittext_invisible_click_watcher).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (AppConfig.DEBUG) {
                            Log.d(mTag, "onClick :: Inside Birthday field");
                        }
                        showDialog(DATE_SELECTION_DIALOG);
                    }
                });

        findViewById(R.id.reg_email_edittext).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.DEBUG) {
                    Log.d(mTag, "onClick :: Inside email field");
                }

            }
        });

        findViewById(R.id.reg_password_edittext).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.DEBUG) {
                    Log.d(mTag, "onClick :: Inside password field");
                }
            }
        });

        maleGenButton = (ImageView) findViewById(R.id.image_gen_male);
        findViewById(R.id.image_gen_male).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.DEBUG) {
                    Log.d(mTag, "onClick :: male status selected");
                }
                maleGenButton.setSelected(true);
                femaleGenButton.setSelected(false);
                sel_gen = "male";
            }
        });

        femaleGenButton = (ImageView) findViewById(R.id.image_gen_female);
        findViewById(R.id.image_gen_female).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.DEBUG) {
                    Log.d(mTag, "onClick :: female status selected");
                }
                maleGenButton.setSelected(false);
                femaleGenButton.setSelected(true);
                sel_gen = "female";
            }
        });

        findViewById(R.id.sign_up_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.DEBUG) {
                    Log.d(mTag, "onClick :: sign up button pressed");
                }
                if (validate()) {
                    onSignUp();
                }
            }
        });

    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2004);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        return dialog;
       // return new DatePickerDialog(this, datePickerListener, year, month, day);
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            edtBirthday.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
            new FinddayFromFile().execute((selectedMonth + 1)+"-"+selectedDay+"-"+selectedYear);
        }
    };

    /* **************************************************************** */
    /* ************** MySQLCommand.OnCompleteListener ***************** */
    /* **************************************************************** */

    @Override
    public void OnComplete(Object result) {
        if (AppConfig.DEBUG) {
            Log.d(mTag, "OnComplete :: result = " + result);
        }
        SignupCommand.SignUpResult b = (SignupCommand.SignUpResult) result;
        if (b.isAuth()) {
            Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, TabFragmentActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignUpActivity.this, b.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /* **************************************************************** */
    /* ************************ Utility API *************************** */
    /* **************************************************************** */

    private boolean validate() {
        if (edtEmail.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please Fill Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!SignUpEventHandler.EmailInputCheck(edtEmail.getText())) {
            Toast.makeText(SignUpActivity.this, "Wrong Input Email Address", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please Fill Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtBirthday.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please Select Birthday", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (sel_gen.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private void onSignUp() {

        String email = edtEmail.getText().toString();
        String name = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        long birthday = mBirthDay.getTimeInMillis();

        MySQLConnect.signup(name, email, password, birthday, sel_gen, this);

        String deviceId = userPhoneIDExport();
    }

    private String userPhoneIDExport() {
        return Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
    }



    class FinddayFromFile extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String Day="";
            try{
                Scanner sc=new Scanner(getResources().openRawResource(R.raw.weekday));
                while(sc.hasNext())
                {
                    String str=sc.nextLine();
                    if(str.contains(params[0]))
                    {
                        Day=str;
                        break;
                    }
                }
                sc.close();
            }catch(Exception e)
            {
                Log.e("error", e.toString());
            }
            if(!Day.equals(""))
            {
                String array[]=Day.split(",");
                Log.d(array[0], array[1]);
                return array[1];
            }
            else
                return "";
        }
        @Override
        protected void onPostExecute(String result) {
            if(!result.equals(""))
                Toast.makeText(SignUpActivity.this,"did you know that you were born on a "+result+"?!" ,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

}
