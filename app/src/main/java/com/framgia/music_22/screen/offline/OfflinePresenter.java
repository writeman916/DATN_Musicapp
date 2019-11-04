package com.framgia.music_22.screen.offline;

import com.framgia.music_22.data.CallBackOfflineSong;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.repository.SongRepository;
import java.util.List;

public class OfflinePresenter implements OfflineContract.Presenter {

    private OfflineContract.View mView;
    private SongRepository mSongRepository;

    public OfflinePresenter(OfflineContract.View view, SongRepository localSongRepository) {
        mView = view;
        mSongRepository = localSongRepository;
    }

    @Override
    public void getSong() {
        mSongRepository.getLocalMusic(new CallBackOfflineSong() {
            @Override
            public void onGetDataSuccess(List<OfflineSong> songList) {
                mView.onGetSongOfflineSuccess(songList);
            }

            @Override
            public void onGetDataError(Exception e) {
                mView.onError(e);
            }
        });
    }

}
