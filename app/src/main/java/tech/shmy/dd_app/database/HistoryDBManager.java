package tech.shmy.dd_app.database;

import android.content.ContentValues;
import android.content.Context;

import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.shmy.dd_app.entity.HistoryEntity;

public class HistoryDBManager {
    private final String tableName = "history";
    private HistoryDBHelper historyDBHelper;
    private SQLiteDatabase db;
    private static Context context;
    private static HistoryDBManager _instance;

    public static void init(Context context) {
        HistoryDBManager.context = context;
    }

    public static HistoryDBManager getInstance() {
        if (_instance == null) {
            synchronized (HistoryDBManager.class) {
                _instance = new HistoryDBManager(HistoryDBManager.context);
            }
        }
        return _instance;
    }

    private HistoryDBManager(Context context) {
        historyDBHelper = new HistoryDBHelper(context);
        db = historyDBHelper.getWritableDatabase();
    }

    public void insert(HistoryEntity historyEntity) {
        ContentValues values = entity2ContentValues(historyEntity);
        values.remove("id");
        long now = new Date().getTime();
        values.put("created_at", now);
        values.put("updated_at", now);
        db.insert(tableName, null, values);
    }

    public void upsertByVid(HistoryEntity historyEntity) {
        HistoryEntity old = findByVid(historyEntity.vid);
        if (old == null) {
            insert(historyEntity);
        } else {
            updateById(old.id, historyEntity);
        }
    }

    public List<HistoryEntity> pagination(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<HistoryEntity> historyEntityList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " ORDER BY updated_at DESC LIMIT " + offset + ", " + pageSize, null);
        while (cursor.moveToNext()) {
            historyEntityList.add(cursor2Entity(cursor));
        }
        return historyEntityList;
    }

    public HistoryEntity findByVid(long vid) {
        Cursor cursor = db.query(tableName, null, "vid= ?", new String[]{vid + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor2Entity(cursor);
        }
        return null;
    }

//    private HistoryEntity findById(long id) {
//        Cursor cursor = db.query(tableName, null, "id= ?", new String[]{id + ""}, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            return cursor2Entity(cursor);
//        }
//        return null;
//    }

    private void updateById(long id, HistoryEntity historyEntity) {
        ContentValues values = entity2ContentValues(historyEntity);
        values.remove("id");
        values.put("updated_at", new Date().getTime());
        db.update(tableName, values, "id = ?", new String[]{id + ""});
    }

    public void deleteById(long id) {
        db.delete(tableName, "id = ?", new String[]{id + ""});
    }

    private HistoryEntity cursor2Entity(Cursor cursor) {
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.id = cursor.getLong(cursor.getColumnIndex("id"));
        historyEntity.vid = cursor.getLong(cursor.getColumnIndex("vid"));
        historyEntity.name = cursor.getString(cursor.getColumnIndex("name"));
        historyEntity.url = cursor.getString(cursor.getColumnIndex("url"));
        historyEntity.pic = cursor.getString(cursor.getColumnIndex("pic"));
        historyEntity.position = cursor.getLong(cursor.getColumnIndex("position"));
        historyEntity.duration = cursor.getLong(cursor.getColumnIndex("duration"));
        historyEntity.created_at = cursor.getLong(cursor.getColumnIndex("created_at"));
        historyEntity.updated_at = cursor.getLong(cursor.getColumnIndex("updated_at"));

        return historyEntity;
    }

    private ContentValues entity2ContentValues(HistoryEntity historyEntity) {
        ContentValues values = new ContentValues();
        values.put("id", historyEntity.id);
        values.put("vid", historyEntity.vid);
        values.put("name", historyEntity.name);
        values.put("url", historyEntity.url);
        values.put("pic", historyEntity.pic);
        values.put("position", historyEntity.position);
        values.put("duration", historyEntity.duration);
        values.put("created_at", historyEntity.created_at);
        values.put("updated_at", historyEntity.updated_at);
        return values;
    }
}
