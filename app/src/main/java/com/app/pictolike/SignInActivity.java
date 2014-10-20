package com.app.pictolike;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.pictolike.Utils.AppConfig;
import com.app.pictolike.data.MyPeople;
import com.app.pictolike.mysql.MySQLCommand;
import com.app.pictolike.mysql.MySQLConnect;

public class SignInActivity extends AbstractAppActivity implements MySQLCommand.OnCompleteListener {

    public static String userNameString;
    private EditText userName;
    private EditText userPassword;
    private RelativeLayout mTopLyt;
    /* **************************************************************** */
    /* ******************* AbstractAppActivity ************************ */
    /* **************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();

        setContentView(R.layout.actvity_signin);

        userName = (EditText) findViewById(R.id.email_edittext);
        userPassword = (EditText) findViewById(R.id.password_edittext);
        mTopLyt =(RelativeLayout) findViewById(R.id.click_lyt);
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
        if (BuildConfig.DEBUG){
            userName.setText("test");
            userPassword.setText("qwerty");
        }
        findViewById(R.id.button_comein).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignInActivity.this, TabFragmentActivity.class);
//                startActivity(intent);
//                finish();
               onSignIn();
            }
        });

        findViewById(R.id.btn_forgot_password).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "Not supported yet", Toast.LENGTH_SHORT).show();
                // Intent i = new Intent(SignInActivity.this,
                // ForgotPasswordScreenActivity.class);
                // SignInActivity.this.startActivity(i);
            }

        });

    }

    /* **************************************************************** */
    /* ************** MySQLCommand.OnCompleteListener ***************** */
    /* **************************************************************** */

    @Override
    public void OnComplete(Object result) {
        if (AppConfig.DEBUG) {
            Log.d(mTag, "OnComplete :: result = " + result);
        }
        MyPeople people = (MyPeople) result;
        if (people == null) {
            Toast.makeText(SignInActivity.this, "Sign in failed username", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent intent = new Intent(SignInActivity.this, TabFragmentActivity.class);
        startActivity(intent);
        finish();
    }

    /* **************************************************************** */
    /* ************************ Utility API *************************** */
    /* **************************************************************** */

    private void onSignIn() {
        if (AppConfig.DEBUG) {
            Log.d(mTag, "onSignIn :: user name = " + userName.getText().toString());
        }
        userNameString = userName.getText().toString();
        if (TextUtils.isEmpty(userNameString)) {
            Toast.makeText(SignInActivity.this, "Please put in the username", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String password = userPassword.getText().toString();
        MySQLConnect.signin(userNameString, password, this);

    }

}
