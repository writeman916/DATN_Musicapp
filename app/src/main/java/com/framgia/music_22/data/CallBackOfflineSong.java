package com.framgia.music_22.data;

import com.framgia.music_22.data.model.OfflineSong;
import java.util.List;

public interface CallBackOfflineSong {
    void onGetDataSuccess(List<OfflineSong> songList);

    void onGetDataError(Exception e);
}
