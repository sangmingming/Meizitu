package me.isming.meizitu.data;

import com.android.volley.*;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

//import me.isming.meizitu.model.Feed.Item;
//import me.isming.meizitu.model.Feed.FeedRequestData;
//import me.isming.meizitu.model.Feed;

public class GsonRequest<T> extends Request<T> {
    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Listener<T> mListener;
    private final Map<String, String> mHeaders;

    public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            json = "[" + json.trim().replaceAll("http.+\\t", "").replaceAll("\\n", ",") + "]";
////            String []splitstring = json.trim().split("\n");
//
//            FeedRequestData feeddata = new FeedRequestData();
//            feeddata.data = new ArrayList<Feed>();
//            for(String topic: splitstring) {
//                Feed item = mGson.fromJson(topic.substring(topic.indexOf("\t") + 1), Feed.class);
//                Feed feed = new Feed();
////                feed.setId(Integer.parseInt(item.date.replaceAll("\\W+", "").substring(4)));
////                feed.setName(item.title);
////                feed.setImgs(item.imgs);
//                feeddata.data.add(feed);
//            }

            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
