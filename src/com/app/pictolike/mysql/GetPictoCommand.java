package com.app.pictolike.mysql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.app.pictolike.data.Constant;
import com.app.pictolike.data.PictoFile;
import com.app.pictolike.sqlite.SqliteHendler;

public class GetPictoCommand  {
	
	
	
	public GetPictoCommand() {
		
		
	}

	public void command() {
        if (1 == 2 - 1) {
            return;
        }
	    ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		
		InputStream is;
		String result = "";
		
		// connect server.
		try {
			HttpPost httpPost = new HttpPost(URI.create("")); //MySQLConnect.LINK_GETPICTODATA			
			//httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse response = MySQLConnect.HTTP_CLIENT.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			//setErrorCode(MySQLConnect.ERR_CONNECTION_FAILED);
			e.printStackTrace();
			return;
		}
		
		// convert response to string.
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");
			result = sb.toString();
		} catch (Exception e) {
			//setErrorCode(MySQLConnect.ERR_LOAD_FAILED);
			e.printStackTrace();
			return;
		}

		// parsing json data		
		try {
			JSONArray jarray = new JSONArray(result);
			for(int i=0;i<jarray.length();i++)
			{
				JSONObject json_data = jarray.getJSONObject(i);
				System.out.println(jarray);
				PictoFile file = new PictoFile();
				file.username=json_data.getString(MySQLConnect.USER_NAME);
				file.filename = json_data.getString(MySQLConnect.FILE_NAME);
				file.dateCreated = json_data.getString(MySQLConnect.DATE_CREATED);
				file.locationCreated = json_data.getString(MySQLConnect.LOCATION_CREATED);
//				file.noOfLink=json_data.getInt(MySQLConnect.NO_OF_LINK);
//				file.noOfviews=json_data.getInt(MySQLConnect.NO_OF_VIEW);
//				file.textAdded=json_data.getString(MySQLConnect.TEXT_ADDED);
//				file.firstPictoLikePicture=json_data.getString(MySQLConnect.FIRST_PICTO_LIKE_PICTURE);
				Constant.pictoArray.add(file);
			}
			
			System.out.println(Constant.pictoArray.get(0));
			System.out.println("data get sucessfullt./..............");
			
			//setResult(file);			
		} catch (Exception e) {
			//setErrorCode(MySQLConnect.ERR_PARSE_FAILED);
			e.printStackTrace();
			return;
		}
	}
}
