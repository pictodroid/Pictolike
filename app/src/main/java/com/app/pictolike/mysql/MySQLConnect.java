package com.app.pictolike.mysql;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URLEncoder;

public class MySQLConnect {

	public static final int CMD_SIGNIN = 1;
	public static final int CMD_SIGNUP = 2;
	public static final int CMD_SAVEFILE = 2;

	
	public static final int ERR_NONE = 0;
	public static final int ERR_CONNECTION_FAILED = -1;
	public static final int ERR_LOAD_FAILED = -2;
	public static final int ERR_USER_EXISTS = -3;
	public static final int ERR_INSERT_FAILED = -4;
	public static final int ERR_PARSE_FAILED = -5;
//    private static final String BASE_URL = "http://winded976.ddns.net";
    private static final String BASE_URL = "http://192.168.1.81";

    public static HttpClient HTTP_CLIENT = new DefaultHttpClient();
	
	public static final String LINK_SIGNIN = String.format("%s/signin.php",BASE_URL);
	public static final String LINK_SIGNUP = String.format("%s/signup.php",BASE_URL);
	public static final String LINK_SENTFILE = String.format("%s/newfile.php",BASE_URL);
	public static final String LINK_GET_HOME_IMAGES = String.format("%s/getPictoData.php",BASE_URL);

	public static final String USER_NAME = "username";
	public static final String FIELD_NAME = "filename";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_PASSWORD = "password";

	public static final String FILE_NAME = "filename";
	public static final String FILE_CONTENT = "filecontent";
	public static final String DATE_CREATED = "datecreated";
	public static final String LOCATION_CREATED = "locationcreated";
	
	public static final String MSG_USER_EXISTS = "user exists";
	public static final String MSG_INSERT_FAILED = "insert failed";
	public static final String FIELD_BIRTHDAY = "birthday";
	public static final String FIELD_GENDER = "gender";
	
	public static final String DEVICEID = "deviceID";
	public static final String IMG221 = "img221B";
	public static final String USERAGE = "usrage";
	
	
	
	static public void signin(String name, String password, MySQLCommand.OnCompleteListener listener) {
		SigninCommand cmd = new SigninCommand(name, password);
		if (listener != null)
			cmd.setOnCompleteListener(listener);
		run_command(cmd);
	}

    static public void signup(String name, String email, String password, long birthday, String gender, MySQLCommand.OnCompleteListener listener) {
        SignupCommand cmd = new SignupCommand(name, email, password, gender, birthday);
        if (listener != null)
            cmd.setOnCompleteListener(listener);
        run_command(cmd);
    }
    

	static public void getPictos( MySQLCommand.OnCompleteListener listener){
        GetPictoCommand cmd = new GetPictoCommand();
        cmd.setOnCompleteListener(listener);
        run_command(cmd);
    }
	static public void savefile(String username,String filename, String datecreated, String locationcreated,  String deviceID, String userage, String gender,String filePath, MySQLCommand.OnCompleteListener listener) {
		SaveFileCommand cmd = new SaveFileCommand(username,filename, datecreated, locationcreated, deviceID, userage, gender,filePath);
		if (listener != null)
			cmd.setOnCompleteListener(listener);
		run_command(cmd);
	}

	static private void run_command(MySQLCommand cmd) {
		new Thread(cmd).start();
	}

    public static String formatImageUrl(final String pFilename) {
        return String.format("%s/getPicto.php?filename=%s",BASE_URL, pFilename.replaceAll(" ","%20"));
    }
}
