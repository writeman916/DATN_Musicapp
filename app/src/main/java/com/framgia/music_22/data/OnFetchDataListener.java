package com.framgia.music_22.data;

public interface OnFetchDataListener {
    void onFetchDataSuccess(String resutlData);

    void onFetchDataError(Exception errorEx);
}
