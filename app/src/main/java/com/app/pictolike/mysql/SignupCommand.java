package com.app.pictolike.mysql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class SignupCommand extends MySQLCommand {
    public static class SignUpResult{
        public static final SignUpResult FAIL = new SignUpResult(false,"Sign up failed");
        private boolean mAuth;
        private String mMessage;
        private String mSession;

        public SignUpResult(final boolean auth, final String message) {
            mAuth = auth;
            mMessage = message;
        }

        public SignUpResult() {

        }

        public boolean isAuth() {
            return mAuth;
        }

        public String getMessage() {
            return mMessage;
        }

        public String getSession() {
            return mSession;
        }
    }
	String m_strUserName;
	String m_strEmail;
	String m_strPassword;
 
	String m_strGender;
	long m_strBirthday;
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	SignupCommand(String name, String email, String password, String gender, long birthday) {
		m_strUserName = name;
		m_strEmail = email;
		m_strPassword = password;
 
		m_strGender= gender;
		m_strBirthday= birthday;
	}

	@Override
	void command() {
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		InputStream is;
		
		// connect server.
		try {
			HttpPost httpPost = new HttpPost(MySQLConnect.LINK_SIGNUP);
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.ACTION, MySQLConnect.ACTION_GET_CODE));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.USER_NAME, m_strUserName));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_EMAIL, m_strEmail));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_PASSWORD, m_strPassword));

            //format timestamp to MySQL friendly date string
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_BIRTHDAY, mSimpleDateFormat.format(m_strBirthday)));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_GENDER, m_strGender));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse response = MySQLConnect.HTTP_CLIENT.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			setErrorCode(MySQLConnect.ERR_CONNECTION_FAILED);
			e.printStackTrace();
			return;
		}
		
		// convert repose to 
		String result = "";
		try {
            StringBuilder lStringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null)
                lStringBuilder.append(line + "\n");
            result = lStringBuilder.toString();
            JSONObject signUpObj = new JSONObject(result);
            SignUpResult lSignUpResult = new SignUpResult();
            lSignUpResult.mAuth = signUpObj.optBoolean("Auth");
            lSignUpResult.mSession = signUpObj.optString("SESSID");
            lSignUpResult.mMessage = signUpObj.optString("Message");
            setResult(lSignUpResult);
            return;
		} catch (Exception e) {
			setErrorCode(MySQLConnect.ERR_LOAD_FAILED);

            setResult(SignUpResult.FAIL);
            e.printStackTrace();
            return;
        }


	}
}
