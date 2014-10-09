package me.isming.meizitu.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.isming.meizitu.util.CLog;


public class DBHelper extends SQLiteOpenHelper {
    // 数据库名
    private static final String DB_NAME = "meizitu.db";

    // 数据库版本
    // 1-->2 add likes table
    private static final int VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CLog.d("Upgrading alarms database from version " + oldVersion + " to " + newVersion);
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgradeTo(db, version);
        }
    }

    private void upgradeTo(SQLiteDatabase db, int version) {
        switch (version) {
            case 1:
                FeedsDataHelper.FeedsDBInfo.TABLE.create(db);
                break;
            case 2:
                LikesDataHelper.FeedsDBInfo.TABLE.create(db);
                break;
            default:
                throw new IllegalStateException("Don't know how to upgrade to " + version);
        }
    }


}
