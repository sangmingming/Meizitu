
package me.isming.meizitu.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * CommonToast<br/>
 * Toast
 * 
 */
public class CToast {

    private static Toast sToast;

    private static Handler mHandler;

    /**
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (sToast == null) {
            mHandler = new Handler(context.getMainLooper());
            sToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
        }
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                sToast.show();
            }
        });
    }

    /**
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        String text = context.getString(resId);
        showToast(context, text);
    }

}
