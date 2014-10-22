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
import com.app.pictolike.Utils.KeyboardHelper;
import com.app.pictolike.dialogs.BirthdaySelectionDialog;
import com.app.pictolike.mysql.MySQLCommand;
import com.app.pictolike.mysql.MySQLConnect;
import com.app.pictolike.mysql.SignupCommand;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import de.greenrobot.event.EventBus;

public class SignUpActivity extends AbstractAppActivity implements MySQLCommand.OnCompleteListener {
    private final SimpleDateFormat BIRTHDATE_FORMAT=new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat WEEKDAY_FORMAT=new SimpleDateFormat("EEEE", Locale.ENGLISH);

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
    private View mSignUpButton;


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
        mTopLyt =(RelativeLayout) findViewById(R.id.lytmain);
        mTopLyt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHelper.hideKeyboard(SignUpActivity.this,getCurrentFocus());
            }
        });
        findViewById(R.id.reg_birthday_edittext_invisible_click_watcher).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (AppConfig.DEBUG) {
                            Log.d(mTag, "onClick :: Inside Birthday field");
                        }
                        BirthdaySelectionDialog lBirthdaySelectionDialog = new BirthdaySelectionDialog();
                        lBirthdaySelectionDialog.setInitialDate(mBirthDay);
                        lBirthdaySelectionDialog.show(getFragmentManager(),"birthday_select");
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

        mSignUpButton = findViewById(R.id.sign_up_btn);
        mSignUpButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.DEBUG) {
                    Log.d(mTag, "onClick :: sign up button pressed");
                }
                mSignUpButton.setEnabled(false);
                if (validate()) {
                    onSignUp();
                } else{
                    mSignUpButton.setEnabled(true);
                }
            }
        });

    }

    @SuppressWarnings("unused")
    /**
     * Event called on UI thread from {@link com.app.pictolike.dialogs.BirthdaySelectionDialog} when date is selected
     */
    public void onEventMainThread(BirthdaySelectionDialog pBirthdaySelectionDialog){
        edtBirthday.setText(BIRTHDATE_FORMAT.format(pBirthdaySelectionDialog.getSelectedDate()));
        mBirthDay.setTime(pBirthdaySelectionDialog.getSelectedDate());

        Toast.makeText(SignUpActivity.this,String.format("did you know that you were born on a %s?!",WEEKDAY_FORMAT.format(mBirthDay.getTime())) ,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    /* **************************************************************** */
    /* ************** MySQLCommand.OnCompleteListener ***************** */
    /* **************************************************************** */

    @Override
    public void OnComplete(Object result) {
        mSignUpButton.setEnabled(true);
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

        String deviceId = userPhoneIDExport();
        MySQLConnect.signup(name, email, password, birthday, sel_gen, this);

    }

    private String userPhoneIDExport() {
        return Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
    }


}
