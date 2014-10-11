package com.app.pictolike;

import android.text.TextUtils;

public class SignUpEventHandler {
	public final static boolean EmailInputCheck(CharSequence target) {
		  if (TextUtils.isEmpty(target)) {
		    return false;
		  } else {
		    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		  }
		}
}
