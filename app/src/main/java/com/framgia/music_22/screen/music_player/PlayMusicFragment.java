package com.framgia.music_22.screen.music_player;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.screen.base.BaseFragment;
import com.framgia.music_22.screen.base.Navigator;
import com.framgia.vnnht.music_22.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicFragment extends BaseFragment
        implements View.OnClickListener, PlayMusicContract.View, MusicServiceContract,
        SeekBar.OnSeekBarChangeListener {

    public static String TAG = "PlayMusicFragment";

    private static final String EXTRA_PLAY_SONG_ONLINE_LIST = "EXTRA_PLAY_SONG_ONLINE_LIST";
    private static final String EXTRA_PLAY_SONG_OFFLINE_LIST = "EXTRA_PLAY_SONG_OFFLINE_LIST";
    private static final String EXTRA_ONLINE_SONG_POSITION = "EXTRA_ONLINE_SONG_POSITION";
    private static final String EXTRA_OFFLINE_SONG_POSITION = "EXTRA_OFFLINE_SONG_POSITION";
    private static final String EXTRA_IS_OFFLINE = "EXTRA_PLAY_SONG_POSITION";
    private static final int REQUEST_PERMISSION_CODE = 69;
    private static final String TIME_FORMAT = "mm:ss";
    private static final int CHECK_LOOP_ONE = 4;

    private AppCompatImageView mButtonPlay, mButtonLoop, mButtonShuffle, mButtonDownload;
    private AppCompatImageView mButtonPlayMini;
    private TextView mTextTitle, mTextArtist, mTextCurrentTime, mTextDuarationTime, mTextArtistMini,
            mTextTitleMini;
    private CircleImageView mImageAvatar, mImageAvatarMini;
    private SeekBar mSeekBarProgressSong;
    //    private Animation mAnimation;
    private MediaPlayer mMediaPlayer;
    private View mInclude, mViewClick;
    private int mCheckShuffleLoop = 1;
    private PlayMusicService mPlayMusicService;
    private ServiceConnection mServiceConnection;
    private Navigator mNavigator;

    public static PlayMusicFragment getInstance() {
        return new PlayMusicFragment();
    }

    public static PlayMusicFragment getOnlineInstance(List<Song> songList, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_PLAY_SONG_ONLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        bundle.putInt(EXTRA_ONLINE_SONG_POSITION, position);
        PlayMusicFragment playMusicFragment = new PlayMusicFragment();
        playMusicFragment.setArguments(bundle);
        return playMusicFragment;
    }

    public static PlayMusicFragment getOfflineInstance(List<OfflineSong> songList, int position,
            boolean check) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_PLAY_SONG_OFFLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        bundle.putInt(EXTRA_OFFLINE_SONG_POSITION, position);
        bundle.putBoolean(EXTRA_IS_OFFLINE, check);
        PlayMusicFragment playMusicFragment = new PlayMusicFragment();
        playMusicFragment.setArguments(bundle);
        return playMusicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        initAnim(view.findViewById(R.id.image_avatar));
        initAnimMini(requireActivity().findViewById(R.id.iv_avatar_mini));
        initView(view);
        return view;
    }

    private ValueAnimator anim;
    private ValueAnimator animMini;

    private void initAnim(View targetView) {
        anim = ObjectAnimator.ofFloat(targetView, View.ROTATION, 0, 360);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(10000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.RESTART);
    }

    private void initAnimMini(View targetView) {
        animMini = ObjectAnimator.ofFloat(targetView, View.ROTATION, 0, 360);
        animMini.setInterpolator(new LinearInterpolator());
        animMini.setDuration(10000);
        animMini.setRepeatCount(ValueAnimator.INFINITE);
        animMini.setRepeatMode(ObjectAnimator.RESTART);
    }

    private void initView(View view) {
        mTextTitle = view.findViewById(R.id.text_title);
        mTextArtist = view.findViewById(R.id.text_artist);
        mTextCurrentTime = view.findViewById(R.id.text_current_time);
        mTextDuarationTime = view.findViewById(R.id.text_duaration_time);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        mSeekBarProgressSong = view.findViewById(R.id.seek_bar_playinh_process);
        AppCompatImageView buttonBack = view.findViewById(R.id.button_back);
        AppCompatImageView buttonPrevious = view.findViewById(R.id.button_previous);
        mButtonPlay = view.findViewById(R.id.button_play);
        AppCompatImageView buttonNext = view.findViewById(R.id.button_next);
        mButtonLoop = view.findViewById(R.id.button_loop);
        mButtonShuffle = view.findViewById(R.id.button_shuffle);
        mButtonDownload = view.findViewById(R.id.button_download);

        // mini player
        mButtonPlayMini = requireActivity().findViewById(R.id.button_play_mini);
        mTextTitleMini = requireActivity().findViewById(R.id.tv_title_mini);
        mTextArtistMini = requireActivity().findViewById(R.id.tv_author_mini);
        AppCompatImageView btnNextMini = requireActivity().findViewById(R.id.button_next_mini);
        AppCompatImageView btnPreviousMini =
                requireActivity().findViewById(R.id.button_previous_mini);
        mImageAvatarMini = requireActivity().findViewById(R.id.iv_avatar_mini);
        mInclude = requireActivity().findViewById(R.id.include_mini_player);
        mViewClick = requireActivity().findViewById(R.id.view_mini_player);
        mNavigator = new Navigator();

        buttonBack.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonPlayMini.setOnClickListener(this);
        btnNextMini.setOnClickListener(this);
        btnPreviousMini.setOnClickListener(this);
        mButtonLoop.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mButtonDownload.setOnClickListener(this);
        mSeekBarProgressSong.setOnSeekBarChangeListener(this);
        onConnectToService();
        initData();
    }

    private void onConnectToService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
                mPlayMusicService = binder.getServiceInstance();
                mPlayMusicService.registerClient(PlayMusicFragment.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mInclude.setVisibility(View.VISIBLE);
                hideFragment();
                break;
            case R.id.button_previous:
                mPlayMusicService.previousSong();
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_previous_mini:
                mPlayMusicService.previousSong();
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_play:
                if (mPlayMusicService.isMusicPlaying()) {
                    mPlayMusicService.pauseSong();
                    mButtonPlay.setImageResource(R.drawable.ic_play_button);
                    //                    mImageAvatar.setAnimation(null);
                    //                    mImageAvatarMini.setAnimation(null);
                    pauseAnimation();
                } else {
                    mPlayMusicService.continueSong();
                    mButtonPlay.setImageResource(R.drawable.ic_pause_button);
                    //                    mImageAvatar.setAnimation(mAnimation);
                    //                    mImageAvatarMini.setAnimation(mAnimation);
                    startOrResumeAnimation();
                }
                break;
            case R.id.button_play_mini:
                if (mPlayMusicService.isMusicPlaying()) {
                    mPlayMusicService.pauseSong();
                    mButtonPlayMini.setImageResource(R.drawable.ic_play_button);
                    //                    mImageAvatar.setAnimation(null);
                    //                    mImageAvatarMini.setAnimation(null);
                    pauseAnimation();
                } else {
                    mPlayMusicService.continueSong();
                    mButtonPlayMini.setImageResource(R.drawable.ic_pause_button);
                    //                    mImageAvatar.setAnimation(mAnimation);
                    //                    mImageAvatarMini.setAnimation(mAnimation);
                    startOrResumeAnimation();
                }
                break;
            case R.id.button_next:
                mPlayMusicService.nextSong(true);
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_next_mini:
                mPlayMusicService.nextSong(true);
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_loop:
                setViewLoopButton();
                break;
            case R.id.button_shuffle:
                setViewShuffleButton();
                break;
            case R.id.button_download:
                onRequestStoragePermission();
                break;
            default:
        }
    }

    private void pauseAnimation() {
        anim.pause();
        animMini.pause();
    }

    private void startOrResumeAnimation() {
        if (anim.isStarted()) {
            anim.resume();
            animMini.resume();
        } else {
            anim.start();
            animMini.start();
        }
    }

    private void hideFragment() {
        mNavigator.hideFragment(getMainActivity().getSupportFragmentManager(),
                PlayMusicFragment.this, false, true);
    }

    private void setViewLoopButton() {
        if (mCheckShuffleLoop == PlayMusicService.CHECK_NO_LOOP) {
            mButtonLoop.setImageResource(R.drawable.ic_repeat_one);
            mCheckShuffleLoop = CHECK_LOOP_ONE;
        } else if (mCheckShuffleLoop == CHECK_LOOP_ONE) {
            mButtonLoop.setImageResource(R.drawable.ic_active_loop_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_LOOP_ALL;
        } else {
            mButtonLoop.setImageResource(R.drawable.ic_unactive_loop_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_NO_LOOP;
        }
        mPlayMusicService.setSuffleLoop(mCheckShuffleLoop);
    }

    private void setViewShuffleButton() {
        if (mCheckShuffleLoop != PlayMusicService.CHECK_SHUFFLE) {
            mButtonShuffle.setImageResource(R.drawable.ic_active_shuffle_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_SHUFFLE;
        } else {
            mButtonShuffle.setImageResource(R.drawable.ic_unactive_shuffle_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_NO_LOOP;
        }
        mPlayMusicService.setSuffleLoop(mCheckShuffleLoop);
    }

    @Override
    public void initData() {
        boolean isOffline = requireActivity().getIntent().getBooleanExtra(EXTRA_IS_OFFLINE, false);
        assert getArguments() != null;
        if (getArguments().getParcelableArrayList(EXTRA_PLAY_SONG_ONLINE_LIST) != null) {
            int position = getArguments().getInt(EXTRA_ONLINE_SONG_POSITION, -1);
            List<Song> songOnlineList =
                    getArguments().getParcelableArrayList(EXTRA_PLAY_SONG_ONLINE_LIST);
            assert songOnlineList != null;
            onPlayMusicOnlineControl(songOnlineList, position);
        } else {
            int position = getArguments().getInt(EXTRA_OFFLINE_SONG_POSITION, -1);
            List<OfflineSong> songOfflineList =
                    getArguments().getParcelableArrayList(EXTRA_PLAY_SONG_OFFLINE_LIST);
            assert songOfflineList != null;
            onPlayMusicOfflineControl(songOfflineList, position, isOffline);
        }
    }

    private void onPlayMusicOnlineControl(List<Song> songs, int position) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextTitleMini.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtist().getSingerName());
        mTextArtistMini.setText(songs.get(position).getArtist().getSingerName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mButtonPlayMini.setImageResource(R.drawable.ic_pause_button);
        //        mImageAvatar.setAnimation(mAnimation);
        startOrResumeAnimation();
        Glide.with(this).load(songs.get(position).getArtist().getAvatarUrl()).into(mImageAvatar);
        Glide.with(this)
                .load(songs.get(position).getArtist().getAvatarUrl())
                .into(mImageAvatarMini);
        onPlayMusicOnline(songs, position);
    }

    private void onPlayMusicOfflineControl(List<OfflineSong> songs, int position,
            boolean isOffline) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextTitleMini.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtistName());
        mTextArtistMini.setText(songs.get(position).getArtistName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mButtonPlayMini.setImageResource(R.drawable.ic_pause_button);
        //        mImageAvatar.setAnimation(mAnimation);
        startOrResumeAnimation();
        mButtonDownload.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.default_avatar_song).into(mImageAvatar);
        Glide.with(this).load(R.drawable.default_avatar_song).into(mImageAvatarMini);
        onPlayMusicOffline(songs, position, isOffline);
    }

    private void onPlayMusicOnline(List<Song> songList, int position) {
        Intent intent = PlayMusicService.getOnlineInstance(requireContext(), songList, position);
        requireActivity().startService(intent);
        requireActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onPlayMusicOffline(List<OfflineSong> songList, int position, boolean isOffline) {
        Intent intent = PlayMusicService.getOfflineInstance(requireContext(), songList, position,
                isOffline);
        requireActivity().startService(intent);
        requireActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void updateMediaToClient(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =
                new SimpleDateFormat(TIME_FORMAT);
        if (mMediaPlayer.isPlaying()) {
            mTextCurrentTime.setText(dateFormat.format(mMediaPlayer.getCurrentPosition()));
            mTextDuarationTime.setText(dateFormat.format(mMediaPlayer.getDuration()));
            mSeekBarProgressSong.setMax(mMediaPlayer.getDuration());
            mButtonPlay.setImageResource(R.drawable.ic_pause_button);
            mButtonPlayMini.setImageResource(R.drawable.ic_pause_button);
            mSeekBarProgressSong.setProgress(mediaPlayer.getCurrentPosition());
            //            mImageAvatar.setAnimation(mAnimation);
            //            mImageAvatarMini.setAnimation(mAnimation);
            startOrResumeAnimation();
        } else {
            mButtonPlay.setImageResource(R.drawable.ic_play_button);
            mButtonPlayMini.setImageResource(R.drawable.ic_play_button);
            //            mImageAvatar.setAnimation(null);
            //            mImageAvatarMini.setAnimation(null);
            pauseAnimation();
        }
    }

    @Override
    public void onUpdateOnlineSongDetail(Song song) {
        mTextTitle.setText(song.getTitle());
        mTextTitleMini.setText(song.getTitle());
        mTextArtist.setText(song.getArtist().getSingerName());
        mTextArtistMini.setText(song.getArtist().getSingerName());
        if (song.getArtist().getAvatarUrl() != null && !song.getArtist().getAvatarUrl().isEmpty()) {
            Glide.with(this)
                    .load(song.getArtist().getAvatarUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.default_avatar_song))
                    .into(mImageAvatarMini);
            Glide.with(this)
                    .load(song.getArtist().getAvatarUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.default_avatar_song))
                    .into(mImageAvatar);
        }
    }

    @Override
    public void onUpdateOfflineSongDetail(OfflineSong song) {
        mTextTitle.setText(song.getTitle());
        mTextTitleMini.setText(song.getTitle());
        mTextArtist.setText(song.getArtistName());
        mTextArtistMini.setText(song.getArtistName());
        Glide.with(this).load(R.drawable.default_avatar_song).into(mImageAvatar);
        Glide.with(this).load(R.drawable.default_avatar_song).into(mImageAvatarMini);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    private void onRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mPlayMusicService.downloadSong();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_PERMISSION_CODE);
        }
    }
}
