package me.isming.meizitu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.isming.meizitu.model.Feed;
import me.isming.meizitu.util.database.Column;
import me.isming.meizitu.util.database.SQLiteTable;

/**
 * Created by sam on 14-10-9.
 */
public class LikesDataHelper extends BaseDataHelper {
    public LikesDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.LIKES_CONTENT_URI;
    }

    private ContentValues getContentValues(Feed feed) {
        ContentValues values = new ContentValues();
        values.put(FeedsDBInfo.ID, feed.getId());
        values.put(FeedsDBInfo.NAME, feed.getName());
        values.put(FeedsDBInfo.TAGS, new Gson().toJson(feed.getTags()));
        values.put(FeedsDBInfo.IMGS, new Gson().toJson(feed.getImgs()));
        return values;
    }

    public Feed query(int id) {
        Feed feed = null;
        Cursor cursor = query(null,  FeedsDBInfo.ID + "= ?",
                new String[] {
                        String.valueOf(id)
                }, null);
        if (cursor.moveToFirst()) {
            feed = Feed.fromCursor(cursor);
        }
        cursor.close();
        return feed;
    }

    public void bulkInsert(List<Feed> feeds) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (Feed feed : feeds) {
            ContentValues values = getContentValues(feed);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public void insert(Feed feed) {
        ContentValues values = getContentValues(feed);
        insert(values);
    }

    public int deleteAll() {
        synchronized (DataProvider.DBLock) {
            DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(FeedsDBInfo.TABLE_NAME, null, null);
            return row;
        }
    }

    public int delete(int id) {
        synchronized (DataProvider.DBLock) {
            DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(FeedsDBInfo.TABLE_NAME, FeedsDBInfo.ID + " = ?", new String[]{String.valueOf(id)});
            return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, FeedsDBInfo.ID + " DESC");
    }

    public static final class FeedsDBInfo implements BaseColumns {
        private FeedsDBInfo() {
        }

        public static final String TABLE_NAME = "likes";

        public static final String ID = "id";

        public static final String NAME = "name";

        public static final String TAGS = "tags";

        public static final String IMGS = "imgs";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(NAME, Column.DataType.TEXT)
                .addColumn(TAGS, Column.DataType.TEXT)
                .addColumn(IMGS, Column.DataType.TEXT);
    }
}
