package com.framgia.music_22.screen.search;

import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.repository.SongRepository;

public class SearchPrecenter implements SearchContract.Presenter {

    private SearchContract.View mView;
    private SongRepository mSongRepository;

    public SearchPrecenter(SearchContract.View view, SongRepository songRepository) {
        mView = view;
        mSongRepository = songRepository;
    }

    @Override
    public void getSongByTitle(String title) {
        mSongRepository.getOnlineMusic(title, new RequestCallbackData<MoreSong>() {
            @Override
            public void onGetDataSuccess(MoreSong moreSong) {
                mView.onGetSongByTitle(moreSong);
            }

            @Override
            public void onGetDataError(Exception e) {
                mView.onError(e);
            }
        });
    }
}
