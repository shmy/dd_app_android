package tech.shmy.dd_app.database;

import android.content.Context;

import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

public class HistoryDBHelper extends SQLiteOpenHelper {
    // 数据库 db 文件名称
    private static final String DEFAULT_NAME = "history.db";
    // 默认版本号
    private static final int DEFAULT_VERSION = 1;
    public HistoryDBHelper(Context context) {
        super(context, DEFAULT_NAME, null, DEFAULT_VERSION, null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT, vid INTEGER, name TEXT, url TEXT, pic TEXT, position INTEGER, duration INTEGER, created_at INTEGER, updated_at INTEGER)";
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }
}
