package com.framgia.music_22.screen.offline;

import com.framgia.music_22.data.model.OfflineSong;
import java.util.List;

public interface OfflineContract {
    /**
     * view
     */
    interface View {
        void onGetSongOfflineSuccess(List<OfflineSong> offlineSongs);

        void onError(Exception ex);
    }

    /**
     * presenter
     */
    interface Presenter {
        void getSong();
    }
}
