package com.framgia.music_22.data.repository;

import com.framgia.music_22.data.CallBackOfflineSong;
import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.SongDataSource;
import com.framgia.music_22.data.model.MoreSong;

public class SongRepository {
    private static SongRepository sInstance;
    private SongDataSource.RemoteDataSource mParseRemoteJsonData;
    private SongDataSource.LocalDataSource mSongLocalDataSource;

    private SongRepository(SongDataSource.RemoteDataSource parseRemoteJsonData,
            SongDataSource.LocalDataSource songLocalDataSource) {
        mParseRemoteJsonData = parseRemoteJsonData;
        mSongLocalDataSource = songLocalDataSource;
    }

    public static synchronized SongRepository getsInstance(
            SongDataSource.RemoteDataSource remoteDataSource,
            SongDataSource.LocalDataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new SongRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    public void getOnlineMusic(String genre, RequestCallbackData<MoreSong> callbackData) {
        mParseRemoteJsonData.getSongByGenre(genre, callbackData);
    }

    public void getLocalMusic(CallBackOfflineSong callbackData) {
        mSongLocalDataSource.getData(callbackData);
    }
}
