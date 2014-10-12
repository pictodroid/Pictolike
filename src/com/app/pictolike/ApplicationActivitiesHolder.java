package com.app.pictolike;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * Fully terminate all activities in application
 */
public class ApplicationActivitiesHolder {

    private static List<Activity> activities = new ArrayList<Activity>();

    private ApplicationActivitiesHolder() {
        /* Prevent instantiation */
    }

    /**
     * Saves reference to target activity.
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * Close application.
     */
    public static void terminate() {
        for (Activity activity : activities) {
            activity.finish();
        }

        android.os.Process.killProcess(android.os.Process.myPid());
    }

}