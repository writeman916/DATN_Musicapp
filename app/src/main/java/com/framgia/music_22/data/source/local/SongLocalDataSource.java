package com.framgia.music_22.data.source.local;

import android.content.Context;
import com.framgia.music_22.data.CallBackOfflineSong;
import com.framgia.music_22.data.SongDataSource;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.source.local.sqlite.DatabaseController;
import java.util.List;

public class SongLocalDataSource implements SongDataSource.LocalDataSource {

    private static SongLocalDataSource sInstance;
    private DatabaseController mDatabaseController;

    private SongLocalDataSource(DatabaseController databaseController) {
        mDatabaseController = databaseController;
    }

    public static synchronized SongLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SongLocalDataSource(new DatabaseController(context));
        }
        return sInstance;
    }

    private List<OfflineSong> onGetDataFromStorage() {
        return mDatabaseController.getListMusicLocal();
    }

    @Override
    public void getData(CallBackOfflineSong callbackData) {
        callbackData.onGetDataSuccess(onGetDataFromStorage());
    }
}
