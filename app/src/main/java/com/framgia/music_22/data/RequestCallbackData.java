package com.framgia.music_22.data;

public interface RequestCallbackData<T> {
    void onGetDataSuccess(T songList);

    void onGetDataError(Exception e);
}
