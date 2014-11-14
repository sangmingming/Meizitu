package me.isming.meizitu.app;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.isming.meizitu.App;
import me.isming.meizitu.data.RequestManager;
import me.isming.meizitu.util.SystemBarTintManager;
import me.isming.meizitu.util.UIUtils;


/**
 * Created by sam on 14-3-24.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //UIUtils.setSystemBarTintColor(this);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            UIUtils.setSystemBarTintColor(this);

        }

//        Window window = getWindow();
//
//        Class clazz = window.getClass();
//        try {
//            int tranceFlag = 0;
//            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//
//            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
//            tranceFlag = field.getInt(layoutParams);
//
//            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
////只需要状态栏透明
//            extraFlagField.invoke(window, tranceFlag, tranceFlag);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(App.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }

}
