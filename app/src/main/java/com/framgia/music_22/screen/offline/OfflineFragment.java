package com.framgia.music_22.screen.offline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.music_22.screen.base.BaseFragment;
import com.framgia.music_22.screen.main.ReloadFragmentCallBack;
import com.framgia.music_22.screen.music_player.PlayMusicFragment;
import com.framgia.music_22.screen.song_list.OnItemClickListener;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class OfflineFragment extends BaseFragment
        implements OfflineContract.View, OnItemClickListener, ReloadFragmentCallBack {

    public static String TAG = "OfflineFragment";
    private List<OfflineSong> mOfflineSongList;
    private RecyclerView mRecyclerSongList;
    private OfflineSongAdapter mAdapter;
    private View mEmptyLayout;

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerSongList = view.findViewById(R.id.recycler_offline_song);
        mEmptyLayout = view.findViewById(R.id.layout_empty);
        mRecyclerSongList.setHasFixedSize(true);
        mAdapter = new OfflineSongAdapter(getContext());
        mRecyclerSongList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        getMainActivity().setReloadFragmentCallBack(this);
    }

    public void initData() {
        SongRemoteDataSource songRemoteDataSource = SongRemoteDataSource.getInstance();
        if (getActivity() != null) {
            SongLocalDataSource songLocalDataSource =
                    SongLocalDataSource.getInstance(getActivity().getApplicationContext());
            SongRepository songRepository =
                    SongRepository.getsInstance(songRemoteDataSource, songLocalDataSource);
            OfflinePresenter presenter = new OfflinePresenter(this, songRepository);
            presenter.getSong();
        }
    }

    @Override
    public void onGetSongOfflineSuccess(List<OfflineSong> offlineSongs) {
        if (!offlineSongs.isEmpty()) {
            mOfflineSongList = offlineSongs;
            mAdapter.updateSongList(mOfflineSongList);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        getMainActivity().addFragment(
                PlayMusicFragment.getOfflineInstance(mOfflineSongList, position, true), true,
                PlayMusicFragment.TAG, false);
    }

    @Override
    public void reloadFragment() {
        initData();
    }
}
