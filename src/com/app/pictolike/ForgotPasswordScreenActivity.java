package com.app.pictolike;
//forgot pass screen: input email
import com.app.pictolike.data.MyPeople;
import com.app.pictolike.mysql.MySQLCommand;
import com.app.pictolike.mysql.MySQLConnect;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ForgotPasswordScreenActivity extends Activity implements AnimationListener {
	ImageView SignIn,Join;
	EditText  userName;
	static String name;
	private MySQLCommand.OnCompleteListener m_oSqlListener = new MySQLCommand.OnCompleteListener() {
		
		@Override
		public void OnComplete(Object result) {
			//check for email existence
			MyPeople people = (MyPeople) result;
			if (people == null) {
				Toast.makeText(ForgotPasswordScreenActivity.this, "Password recovery failed", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(ForgotPasswordScreenActivity.this, SignInActivity.class);
			startActivity(intent);
			finish();
		}
	};
	

    LinearLayout signInActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		/** Code to change Action Bar Color */
		ActionBar bar = getActionBar();
		bar.hide();
		ColorDrawable cd = new ColorDrawable(0xFFFBAC00);
		bar.setBackgroundDrawable(cd);
		
		setContentView(R.layout.actvity_signin);
//		SignIn=(ImageView)findViewById(R.id.Login);
//		
//		signInActivity=(LinearLayout)findViewById(R.id.signInAcitivty);

//		Join=(ImageView)findViewById(R.id.JoinUs);
//		userName=(EditText)findViewById(R.id.Email);
//		password=(EditText)findViewById(R.id.Password);
//		SignIn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// Toast.makeText(getApplicationContext(),"Email Sent to "+ userName.getText(), Toast.LENGTH_SHORT).show();
//				OnSignIn();
//			}
//		});
//		
//		Join.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent joinUsActivity=new Intent(SignInActivity.this,SignUpActivity.class);
//				startActivity(joinUsActivity);
//				
//			}
//		});
	}



	
	private void OnSignIn() {
		 name = userName.getText().toString();
		//String password1 = password.getText().toString();
//		MySQLConnect.signin(name, m_oSqlListener);  //needs to be changed for forgotpassword method
	}




	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
