package me.isming.meizitu.model;

import com.google.gson.Gson;

/**
 * Created by sam on 14-4-22.
 */
public class BaseModel {

    public String toJson() {
        return new Gson().toJson(this);
    }
}
