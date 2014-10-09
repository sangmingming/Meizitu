package me.isming.meizitu.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import me.isming.meizitu.App;
import me.isming.meizitu.data.RequestManager;


/**
 * Created by sam on 14-3-24.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
