package com.framgia.music_22.screen.song_list;

import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.repository.SongRepository;

public class SongByGenrePresenter implements SongByGenreContract.Presenter {

    private SongByGenreContract.View mView;
    private SongRepository mSongRepository;

    public SongByGenrePresenter(SongByGenreContract.View view, SongRepository songRepository) {
        mView = view;
        mSongRepository = songRepository;
    }

    @Override
    public void getSongByGenres(String genre) {
        mSongRepository.getOnlineMusic(genre, new RequestCallbackData<MoreSong>() {
            @Override
            public void onGetDataSuccess(MoreSong moreSong) {
                mView.onGetSongByGenreSuccess(moreSong);
            }

            @Override
            public void onGetDataError(Exception e) {
                mView.onError(e);
            }
        });
    }
}
