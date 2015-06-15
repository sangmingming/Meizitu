package me.isming.meizitu.model;

import android.database.Cursor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.isming.meizitu.dao.FeedsDataHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sam on 14-4-22.
 */
public class Feed extends BaseModel {
    private UUID id;
    private Map<String, String> author;
    private String date;
    private ArrayList<String> imgs;
    private String title;
    private String url;

    public Map<String, String> getAuthor() {
        return author;
    }

    public void setAuthor(Map<String, String> author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Feed() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public ArrayList<String> getTags() {
//        return tags;
//    }
//
//    public void setTags(ArrayList<String> tags) {
//        this.tags = tags;
//    }

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
        feed.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.ID))));
        feed.setTitle(cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.TITLE)));
        feed.setAuthor((Map<String, String>) new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.AUTHOR)),
                new TypeToken<Map<String, String>>() {
                }.getType()));
        feed.setImgs((ArrayList<String>) new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.IMGS)),
                new TypeToken<List<String>>() {
                }.getType()));
        feed.setDate(cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.DATE)));
        feed.setUrl(cursor.getString(cursor.getColumnIndex(FeedsDataHelper.FeedsDBInfo.URL)));
        return feed;
    }

    public static class FeedRequestData {
//        public int status;
//        public String msg;
        public ArrayList<Feed> data;

    }

//    public static class Item {
////        public Result result;
////        public static class Result {
//            public Map<String, String> author;
//            public String date;
//            public ArrayList<String> imgs;
//            public String title;
////            public String taskid;
////            public String updatetime;
//            public String url;
////        }
//    }
}
