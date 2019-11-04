package com.framgia.music_22.data.source.local.sqlite;

import android.content.Context;
import com.framgia.music_22.data.model.OfflineSong;
import java.util.List;

public class DatabaseController {
   private Context mContext;
   private DatabaseSQLite mDatabaseSQLite;

    public DatabaseController(Context context ) {
        mContext = context;
        mDatabaseSQLite = new DatabaseSQLite(mContext);
    }

    public List<OfflineSong> getListMusicLocal(){
       return mDatabaseSQLite.getListMusicLocal();
    }
}
