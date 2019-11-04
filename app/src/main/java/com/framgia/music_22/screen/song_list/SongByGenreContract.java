package com.framgia.music_22.screen.song_list;

import com.framgia.music_22.data.model.MoreSong;

public interface SongByGenreContract {
    /**
     * view
     */
    interface View {
        void onGetSongByGenreSuccess(MoreSong moreSong);

        void onError(Exception ex);
    }

    /**
     * presenter
     */
    interface Presenter {
        void getSongByGenres(String genre);
    }
}
