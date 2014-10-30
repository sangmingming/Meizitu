package me.isming.meizitu.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;

import me.isming.meizitu.app.R;

/**
 * Created by sam on 14-10-30.
 */
public class UIUtils {
    public static void setSystemBarTintColor(Activity activity){
        if(SystemBarTintManager.isKitKat()){
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintDrawable(new ColorDrawable(activity.getResources().getColor(R.color.material_700)));
        }
    }
}
