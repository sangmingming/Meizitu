package me.isming.meizitu.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;


public class ActionBarUtils {
    public static View findActionBarContainer(Activity activity) {
        if(Build.VERSION.SDK_INT > 11) {
            int id = activity.getResources().getIdentifier("action_bar_container", "id", "android");
            return activity.findViewById(id);
        } else {
            return null;
        }
    }

    public static View findSplitActionBar(Activity activity) {
        int id = activity.getResources().getIdentifier("split_action_bar", "id", "android");
        return activity.findViewById(id);
    }
}
