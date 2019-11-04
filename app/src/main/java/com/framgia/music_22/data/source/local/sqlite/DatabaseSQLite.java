package com.framgia.music_22.data.source.local.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSQLite extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "SongOfflineData";
    private static final String DATABASE_LOCAL_MUSIC = "DATABASE_LOCAL_MUSIC.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    public DatabaseSQLite(Context context) {
        super(context, DATABASE_LOCAL_MUSIC, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {

    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor getDataFromDatabase(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<OfflineSong> getListMusicLocal() {
        List<OfflineSong> songList = new ArrayList<>();
        OfflineSong offlineSong;
        String sql = "SELECT * FROM " + TABLE_NAME;
        DatabaseSQLite databaseSQLite = new DatabaseSQLite(mContext);
        Cursor cursor = databaseSQLite.getDataFromDatabase(sql);
        while (cursor.moveToNext()) {
            offlineSong = new OfflineSong.Builder().withSongId(cursor.getString(0))
                    .withTitle(cursor.getString(1))
                    .withGenre(cursor.getString(2))
                    .withSongPath(cursor.getString(3))
                    .withAritstName(cursor.getString(4))
                    .withDuaration(cursor.getInt(5))
                    .build();
            songList.add(offlineSong);
        }
        return songList;
    }
}
