package me.isming.meizitu.model;

import android.database.Cursor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.isming.meizitu.dao.FeedsDataHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 14-4-22.
 */
public class Feed extends BaseModel {
    private int id;
    private String name;
    private ArrayList<String> tags;
    private ArrayList<String> imgs;

    public Feed() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public static Feed fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, Feed.class);
    }

    public static Feed fromCursor(Cursor cursor) {
        Feed feed = new Feed();
        feed.setId(cursor.getInt(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.ID)));
        feed.setName(cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.NAME)));
        feed.setTags((ArrayList<String>) new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.TAGS)),
                new TypeToken<List<String>>(){}.getType()));
        feed.setImgs((ArrayList<String>) new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.IMGS)),
                new TypeToken<List<String>>(){}.getType()));
        return feed;
    }

    public static class FeedRequestData {
        public int status;
        public String msg;
        public ArrayList<Feed> data;

    }
}
