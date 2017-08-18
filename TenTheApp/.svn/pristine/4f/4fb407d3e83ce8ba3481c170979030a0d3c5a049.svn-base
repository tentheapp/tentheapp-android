package com.nvcomputers.ten.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by rkumar4 on 8/3/2016.
 */

public class IntentHandler {

    public static void switchActivity(Activity activity, Class<?> callingActivity) {
        Intent intent = new Intent(activity, callingActivity);
        activity.startActivity(intent);
    }

    public static void switchActivity(Activity activity, Class<?> callingActivity, Bundle extras) {
        Intent intent = new Intent(activity, callingActivity);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    public static void switchActivityFinish(Activity activity, Class<?> callingActivity) {
        Intent intent = new Intent(activity, callingActivity);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void switchActivityFinish(Activity activity, Class<?> callingActivity, Bundle extras) {
        Intent intent = new Intent(activity, callingActivity);
        intent.putExtras(extras);
        activity.startActivity(intent);
        activity.finish();
    }
}
