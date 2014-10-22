package com.app.pictolike;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Base application activity. Holds functionality which is common for all
 * application activities. <br/>
 * Added field mTag, which will hold name of each concrete activity and can be
 * reused in all descendants for logging.
 * 
 */
public class AbstractAppActivity extends Activity {

    protected final String mTag = getClass().getSimpleName();

    /* **************************************************************** */
    /* *************************** Activity *************************** */
    /* **************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationActivitiesHolder.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
//        super.onBackPressed();
    }
}
