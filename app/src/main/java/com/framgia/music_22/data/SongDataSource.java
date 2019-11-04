package com.framgia.music_22.data;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;
import java.util.List;

public interface SongDataSource {
    interface RemoteDataSource {
        void getSongByGenre(String genre, RequestCallbackData<MoreSong> callbackData);
    }

    interface LocalDataSource {
        void getData(CallBackOfflineSong callbackData);
    }
}
