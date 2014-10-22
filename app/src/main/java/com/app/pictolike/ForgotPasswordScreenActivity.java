package com.app.pictolike;

//forgot pass screen: input email
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.pictolike.Utils.AppConfig;
import com.app.pictolike.data.MyPeople;
import com.app.pictolike.mysql.MySQLCommand;

public class ForgotPasswordScreenActivity extends AbstractAppActivity implements
        MySQLCommand.OnCompleteListener {

    private ImageView signIn;
    private ImageView join;
    private EditText userName;
    private String name;

    /* **************************************************************** */
    /* ******************* AbstractAppActivity ************************ */
    /* **************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        /** Code to change Action Bar Color */
        ActionBar bar = getActionBar();
        bar.hide();
        ColorDrawable cd = new ColorDrawable(0xFFFBAC00);
        bar.setBackgroundDrawable(cd);

        setContentView(R.layout.actvity_forgotpassword);
        // SignIn=(ImageView)findViewById(R.id.Login);
        //
        // signInActivity=(LinearLayout)findViewById(R.id.signInAcitivty);

        // Join=(ImageView)findViewById(R.id.JoinUs);
        // userName=(EditText)findViewById(R.id.Email);
        // password=(EditText)findViewById(R.id.Password);
        // SignIn.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // // Toast.makeText(getApplicationContext(),"Email Sent to "+
        // userName.getText(), Toast.LENGTH_SHORT).show();
        // OnSignIn();
        // }
        // });
        //
        // Join.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // Intent joinUsActivity=new
        // Intent(SignInActivity.this,SignUpActivity.class);
        // startActivity(joinUsActivity);
        //
        // }
        // });
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
            Toast.makeText(ForgotPasswordScreenActivity.this, "Password recovery failed",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ForgotPasswordScreenActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    /* **************************************************************** */
    /* ************************ Utility API *************************** */
    /* **************************************************************** */

    private void OnSignIn() {
        name = userName.getText().toString();
        // String password1 = password.getText().toString();
        // MySQLConnect.signin(name, m_oSqlListener); //needs to be changed for
        // forgotpassword method
    }

}
