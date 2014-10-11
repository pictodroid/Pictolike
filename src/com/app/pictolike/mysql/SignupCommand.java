package com.app.pictolike.mysql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

class SignupCommand extends MySQLCommand {
	String m_strUserName;
	String m_strEmail;
	String m_strPassword;
 
	String m_strGender;
	String m_strBirthday;

	SignupCommand(String name, String email, String password, String gender, String birthday) {
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
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.USER_NAME, m_strUserName));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_EMAIL, m_strEmail));
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_PASSWORD, m_strPassword));
 
			nameValuePair.add(new BasicNameValuePair(MySQLConnect.FIELD_BIRTHDAY, m_strBirthday));
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			result = reader.readLine();
		} catch (Exception e) {
			setErrorCode(MySQLConnect.ERR_LOAD_FAILED);
			e.printStackTrace();
			return;
		}
		
		if (result == null)
			setResult(Boolean.TRUE);
		else if (result.equals(MySQLConnect.MSG_USER_EXISTS)) {
			setErrorCode(MySQLConnect.ERR_USER_EXISTS);
			setResult(Boolean.FALSE);
		} else if (result.equals(MySQLConnect.MSG_INSERT_FAILED)) {
			setErrorCode(MySQLConnect.ERR_INSERT_FAILED);
			setResult(Boolean.FALSE);
		}			
	}
}
