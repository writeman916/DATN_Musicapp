package com.framgia.music_22.screen.search;

import com.framgia.music_22.data.model.MoreSong;

public interface SearchContract {
    /**
     * view
     */
    interface View {
        void onGetSongByTitle(MoreSong moreSong);

        void onError(Exception ex);
    }

    /**
     * presenter
     */
    interface Presenter {
        void getSongByTitle(String title);
    }
}
