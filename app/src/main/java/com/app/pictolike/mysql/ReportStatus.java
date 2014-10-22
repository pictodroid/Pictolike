package com.app.pictolike.mysql;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 * Created by mustafa on 21/10/14.
 */
public class ReportStatus {
    private static final String TAG = ReportStatus.class.getSimpleName();
    private static final String REPORT_URL = "http://93.139.157.173:81/picto/report_status.php";
    public static void reportStatus(final boolean status) {
        new Thread(new Runnable() {
            @Override
            public void run() {
// connect to the web server
                try {
                    HttpGet httpGet = new HttpGet(REPORT_URL);
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(httpGet);
                    Log.i(TAG, "Response status is: " + response.getStatusLine());
                } catch (Exception e) {
                    Log.e(TAG, "Error sending command to the server", e);
                }
            }
        }).start();
    }
}
