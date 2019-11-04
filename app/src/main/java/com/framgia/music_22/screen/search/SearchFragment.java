package com.framgia.music_22.screen.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.music_22.screen.base.BaseFragment;
import com.framgia.music_22.screen.base.Navigator;
import com.framgia.music_22.screen.music_player.PlayMusicFragment;
import com.framgia.music_22.screen.song_list.OnItemClickListener;
import com.framgia.music_22.screen.song_list.SongByGenreAdapter;
import com.framgia.music_22.utils.ConnectionChecking;
import com.framgia.music_22.utils.Constant;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class SearchFragment extends BaseFragment
        implements SearchContract.View, View.OnClickListener, OnItemClickListener {

    public static final String TAG = "SearchFragment";
    private static final String LINK_TO_SERACH = "&q=";

    private EditText mEditSearchBox;
    private RecyclerView mRecyclerSearchedList;
    private SearchContract.Presenter mPresenter;
    private SongByGenreAdapter mAdapter;
    private List<Song> mSongList;
    private ConnectionChecking mConnectionChecking;
    private ProgressBar mProgressBar;
    private View mEmptyLayout;
    private Navigator mNavigator = new Navigator();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mEditSearchBox = view.findViewById(R.id.edit_search_box);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mEmptyLayout = view.findViewById(R.id.layout_empty);
        ImageButton buttonSearch = view.findViewById(R.id.button_search);
        mRecyclerSearchedList = view.findViewById(R.id.recycler_searched_song);
        mConnectionChecking = new ConnectionChecking(getContext().getApplicationContext());
        mRecyclerSearchedList.setHasFixedSize(true);
        mAdapter = new SongByGenreAdapter(getContext());
        mAdapter.setOnItemClickListener(this);
        buttonSearch.setOnClickListener(this);

        initData();
    }

    @Override
    public void initData() {
        SongRepository songRepository =
                SongRepository.getsInstance(SongRemoteDataSource.getInstance(),
                        SongLocalDataSource.getInstance(getContext().getApplicationContext()));
        mPresenter = new SearchPrecenter(this, songRepository);
    }

    @Override
    public void onGetSongByTitle(MoreSong moreSong) {
        if (moreSong != null && moreSong.getSongsList() != null) {
            mSongList = moreSong.getSongsList();
            mAdapter.updateSongList(mSongList);
            mRecyclerSearchedList.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        String keyName = mEditSearchBox.getText().toString();
        mNavigator.hideKeyBoard(requireActivity());
        if (keyName.isEmpty()) {
            mEditSearchBox.setError(getResources().getString(R.string.text_search_inform));
        } else if (!mConnectionChecking.isNetworkConnection()) {
            Toast.makeText(getContext(), R.string.text_connection_information, Toast.LENGTH_SHORT)
                    .show();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mPresenter.getSongByTitle(Constant.GENRES_URL + LINK_TO_SERACH + keyName);
        }
    }

    @Override
    public void onItemClick(int position) {
        mNavigator.hideKeyBoard(requireActivity());
        getMainActivity().addFragment(PlayMusicFragment.getOnlineInstance(mSongList, position),
                true, PlayMusicFragment.TAG, false);
    }
}
