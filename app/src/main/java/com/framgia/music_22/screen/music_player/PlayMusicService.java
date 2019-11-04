package com.framgia.music_22.screen.music_player;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.source.local.sqlite.DatabaseSQLite;
import com.framgia.music_22.screen.main.MainActivity;
import com.framgia.music_22.screen.main.NotificationCallBack;
import com.framgia.music_22.screen.main.NotificationReceiver;
import com.framgia.music_22.utils.ConnectionChecking;
import com.framgia.music_22.utils.Constant;
import com.framgia.vnnht.music_22.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class PlayMusicService extends Service implements NotificationCallBack {

    public static final int CHECK_NO_LOOP = 1;
    public static final int CHECK_LOOP_ALL = 2;
    public static final int CHECK_SHUFFLE = 3;

    private String sChannelId = "music_app_id";
    private static final String sChannelName = "Music_App_Name";

    private static final String EXTRA_PLAY_SONG_ONLINE_LIST = "EXTRA_PLAY_SONG_ONLINE_LIST";
    private static final String EXTRA_PLAY_SONG_OFFLINE_LIST = "EXTRA_PLAY_SONG_OFFLINE_LIST";
    private static final String EXTRA_MUSIC_POSITION = "EXTRA_MUSIC_POSITION";
    private static final String EXTRA_IS_OFFLINE = "EXTRA_PLAY_SONG_POSITION";
    private static final int DELAY_TIME = 1000;
    private static final String FILE_DIR = "file://\" + \"/sdcard/Music/";
    private static final String MP3_FORMAT = ".mp3";
    private static final int ID_FOREGROUND_SERVICE = 1;

    private MusicServiceContract mView;
    private ConnectionChecking mConnectionChecking;
    private int mCheckShuffleLoop = 1;
    private IBinder mIBinder = new LocalBinder();
    private int mPosition = 0;
    private MediaPlayer mMediaPlayer;
    private List<Song> mOnlineSongList = new ArrayList<>();
    private List<OfflineSong> mOfflineSongList = new ArrayList<>();
    private boolean mIsOffline;
    private NotificationReceiver mNotificationReceiver;

    DatabaseSQLite mDatabaseSQLite = new DatabaseSQLite(this);

    public static Intent getOnlineInstance(Context context, List<Song> songList, int position) {
        Intent intent = new Intent(context, PlayMusicService.class);
        intent.putParcelableArrayListExtra(EXTRA_PLAY_SONG_ONLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_MUSIC_POSITION, position);
        return intent;
    }

    public static Intent getOfflineInstance(Context context, List<OfflineSong> songList,
            int position, boolean isOffline) {
        Intent intent = new Intent(context, PlayMusicService.class);
        intent.putParcelableArrayListExtra(EXTRA_PLAY_SONG_OFFLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_MUSIC_POSITION, position);
        intent.putExtra(EXTRA_IS_OFFLINE, isOffline);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    private void initData() {
        mConnectionChecking = new ConnectionChecking(this.getApplicationContext());
        mNotificationReceiver = new NotificationReceiver(this);

        registerReceiver(mNotificationReceiver, new IntentFilter(Constant.ACTION_PLAY_PAUSE));
        registerReceiver(mNotificationReceiver, new IntentFilter(Constant.ACTION_PREVIOUS));
        registerReceiver(mNotificationReceiver, new IntentFilter(Constant.ACTION_PLAY_NEXT));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        int position = intent.getIntExtra(EXTRA_MUSIC_POSITION, 0);
        mIsOffline = intent.getBooleanExtra(EXTRA_IS_OFFLINE, false);
        if (intent.getParcelableArrayListExtra(EXTRA_PLAY_SONG_ONLINE_LIST) != null) {
            mOnlineSongList = intent.getParcelableArrayListExtra(EXTRA_PLAY_SONG_ONLINE_LIST);
            onPlayMusicOnlineService(mOnlineSongList, position);
        } else {
            mOfflineSongList = intent.getParcelableArrayListExtra(EXTRA_PLAY_SONG_OFFLINE_LIST);
            onPlayMusicOfflineService(mOfflineSongList, position);
        }
        return START_NOT_STICKY;
    }

    public void onPlayMusicOnlineService(List<Song> songList, int position) {
        mPosition = position;
        mMediaPlayer = new MediaPlayer();
        if (mConnectionChecking.isNetworkConnection()) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(
                        songList.get(mPosition).getStreamUrl() + Constant.CLIENT_ID);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.updateMediaToClient(mMediaPlayer);
                                handler.postDelayed(this, DELAY_TIME);
                                mediaPlayer.setOnCompletionListener(
                                        new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mediaPlayer) {
                                                nextSong(false);
                                            }
                                        });
                            }
                        }, DELAY_TIME);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadBitmapToStartNotification(false);
        } else {
            Toast.makeText(this, getResources().getString(R.string.text_connection_information),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onPlayMusicOfflineService(List<OfflineSong> songList, int position) {
        mPosition = position;
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(songList.get(mPosition).getSongPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mView.updateMediaToClient(mMediaPlayer);
                            handler.postDelayed(this, DELAY_TIME);
                            mediaPlayer.setOnCompletionListener(
                                    new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            nextSong(false);
                                        }
                                    });
                        }
                    }, DELAY_TIME);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadBitmapToStartNotification(false);
    }

    public void registerClient(MusicServiceContract musicServiceContract) {
        mView = musicServiceContract;
    }

    class LocalBinder extends Binder {
        PlayMusicService getServiceInstance() {
            return PlayMusicService.this;
        }
    }

    public boolean isMusicPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pauseSong() {
        mMediaPlayer.pause();
        pausePlayIcon = R.drawable.ic_play_arrow_black_24dp;
        loadBitmapToStartNotification(true);
    }

    public void continueSong() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            pausePlayIcon = R.drawable.ic_pause_black_24dp;
            loadBitmapToStartNotification(false);
        }
    }

    public void nextSong(boolean isClickPause) {
        mMediaPlayer.release();
        if (!mIsOffline) {
            onPlayMusicOnlineService(mOnlineSongList,
                    onLoopShuffleSong(mOnlineSongList.size(), mCheckShuffleLoop, isClickPause));
            mView.onUpdateOnlineSongDetail(mOnlineSongList.get(mPosition));
        } else {
            onPlayMusicOfflineService(mOfflineSongList,
                    onLoopShuffleSong(mOfflineSongList.size(), mCheckShuffleLoop, isClickPause));
            mView.onUpdateOfflineSongDetail(mOfflineSongList.get(mPosition));
        }
    }

    public void previousSong() {
        mMediaPlayer.release();
        if (!mIsOffline) {
            if (mPosition == 0) {
                mPosition = mOnlineSongList.size() - 1;
            } else {
                mPosition--;
            }
            onPlayMusicOnlineService(mOnlineSongList, mPosition);
            mView.onUpdateOnlineSongDetail(mOnlineSongList.get(mPosition));
        } else {
            if (mPosition == 0) {
                mPosition = mOfflineSongList.size() - 1;
            } else {
                mPosition--;
            }
            onPlayMusicOfflineService(mOfflineSongList, mPosition);
            mView.onUpdateOfflineSongDetail(mOfflineSongList.get(mPosition));
        }
    }

    public void setSuffleLoop(int check) {
        mCheckShuffleLoop = check;
    }

    /**
     * check = 1: no loop, index will run to last element
     * check = 2 : loop all ,index will run to last element and comback first element
     * check = 3 : shuffle
     * else : loop 1
     */
    public int onLoopShuffleSong(int listSize, int check, boolean isClickNext) {
        if ((check == CHECK_NO_LOOP || isClickNext) && mPosition < listSize - 1) {
            mPosition++;
        } else if (check == CHECK_LOOP_ALL) {
            if (mPosition == listSize - 1) {
                mPosition = 0;
            } else {
                mPosition++;
            }
        } else if (check == CHECK_SHUFFLE) {
            Random random = new Random();
            mPosition = random.nextInt(listSize - 1);
        } else if (mPosition == listSize - 1) {
            Toast.makeText(this, getResources().getString(R.string.text_last_song_if),
                    Toast.LENGTH_SHORT).show();
        }
        return mPosition;
    }

    public void downloadSong() {
        if (checkDowloaded()) {
            Toast.makeText(this, R.string.text_inforn_downloaded_song, Toast.LENGTH_SHORT).show();
        } else {
            DownloadManager downloadManager =
                    (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uriStream =
                    Uri.parse(mOnlineSongList.get(mPosition).getStreamUrl() + Constant.CLIENT_ID);
            String stringDir = FILE_DIR + mOnlineSongList.get(mPosition).getTitle() + MP3_FORMAT;
            DownloadManager.Request request = new DownloadManager.Request(uriStream);
            request.setTitle(
                    getString(R.string.text_download) + mOnlineSongList.get(mPosition).getTitle());
            request.setDescription(getString(R.string.text_dowloading));
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationUri(Uri.parse(stringDir));
            assert downloadManager != null;
            downloadManager.enqueue(request);
            onSaveSongDetail(stringDir);
        }
    }

    public void onSaveSongDetail(String songDir) {
        Song song = mOnlineSongList.get(mPosition);
        mOfflineSongList = new ArrayList<>();
        OfflineSong offlineSong = new OfflineSong.Builder().withSongId(song.getSongId())
                .withTitle(song.getTitle())
                .withGenre(song.getGenre())
                .withSongPath(songDir)
                .withDuaration(song.getDuaration())
                .withAritstName(song.getArtist().getSingerName())
                .build();
        mDatabaseSQLite.queryData("INSERT INTO "
                + DatabaseSQLite.TABLE_NAME
                + " VALUES ('"
                + offlineSong.getSongId()
                + "' , '"
                + offlineSong.getTitle()
                + "' , '"
                + offlineSong.getGenre()
                + "' , '"
                + offlineSong.getSongPath()
                + "' , '"
                + offlineSong.getArtistName()
                + "' , "
                + offlineSong.getDuaration()
                + " )");
    }

    private boolean checkDowloaded() {
        boolean wasDownloaded = false;
        Cursor cursor = mDatabaseSQLite.getDataFromDatabase(
                "SELECT * FROM " + DatabaseSQLite.TABLE_NAME + " WHERE SongId = '" + mOnlineSongList
                        .get(mPosition)
                        .getSongId() + "'");
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            wasDownloaded = true;
        }
        return wasDownloaded;
    }


    /* --------NOTIFICATION AREA  --------------- */

    /**
     * Load Avatar bitmap -> startForeground
     */

    private int pausePlayIcon = R.drawable.ic_pause_black_24dp;

    private Observable<Bitmap> getBitmapArtistAvatar(final String urlString) {
        return Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                URL url = new URL(urlString);
                InputStream inputStream = url.openConnection().getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            }
        });
    }

    private void loadBitmapToStartNotification(final Boolean isThenStop) {
        Disposable disposable = getBitmapArtistAvatar(
                mOnlineSongList.get(mPosition).getArtist().getAvatarUrl()).subscribeOn(
                Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        startForeground(ID_FOREGROUND_SERVICE, initForegroundService(bitmap));
                        if (isThenStop) {
                            stopForeground(false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        startForeground(ID_FOREGROUND_SERVICE, initForegroundService(null));
                    }
                });
    }

    public Notification initForegroundService(Bitmap bitmap) {

        Intent iPre = new Intent(Constant.ACTION_PREVIOUS);
        Intent iPause = new Intent(Constant.ACTION_PLAY_PAUSE);
        Intent iNext = new Intent(Constant.ACTION_PLAY_NEXT);

        PendingIntent pPrevious =
                PendingIntent.getBroadcast(this, 0, iPre, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pPause =
                PendingIntent.getBroadcast(this, 0, iPause, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pNext =
                PendingIntent.getBroadcast(this, 0, iNext, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action actionPrevious =
                new NotificationCompat.Action.Builder(R.drawable.ic_skip_previous_black_24dp,
                        Constant.PREVIOUS, pPrevious).build();
        NotificationCompat.Action actionPause =
                new NotificationCompat.Action.Builder(pausePlayIcon, Constant.PAUSE,
                        pPause).build();
        NotificationCompat.Action actionNext =
                new NotificationCompat.Action.Builder(R.drawable.ic_skip_next_black_24dp,
                        Constant.NEXT, pNext).build();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constant.GO_TO_PLAYER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sChannelId = createNotificationChannelAndroidO(sChannelId, sChannelName);
        }

        Notification notification;
        if (!mIsOffline) {
            notification = new NotificationCompat.Builder(this, sChannelId).setContentTitle(
                    getString(R.string.text_notification_playing))
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.ic_notification_playing)
                    .setLargeIcon(bitmap)
                    .setContentTitle(mOnlineSongList.get(mPosition).getTitle())
                    .setContentIntent(pendingIntent)
                    .addAction(actionPrevious)
                    .addAction(actionPause)
                    .addAction(actionNext)
                    .setDefaults(0)
                    .setColor(Color.GRAY)
                    .setStyle(
                            new android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(
                                    0, 1, 2))
                    .build();
        } else {
            Bitmap bitmapIcon = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.default_avatar_song);
            notification = new NotificationCompat.Builder(this, sChannelId).setContentTitle(
                    getString(R.string.text_notification_playing))
                    .setAutoCancel(true)
                    .setLargeIcon(bitmapIcon)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.ic_notification_playing)
                    .setContentTitle(mOfflineSongList.get(mPosition).getTitle())
                    .setContentIntent(pendingIntent)
                    .addAction(actionPrevious)
                    .addAction(actionPause)
                    .addAction(actionNext)
                    .build();
        }

        return notification;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannelAndroidO(String channelId, String channelName) {
        NotificationChannel channel =
                new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        channel.enableLights(true);
        channel.setSound(null, null);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    @Override
    public void onBackClicked() {
        previousSong();
    }

    @Override
    public void onPauseClicked() {
        if (mMediaPlayer.isPlaying()) {
            pauseSong();
        } else {
            continueSong();
        }
    }

    @Override
    public void onNextClicked() {
        nextSong(true);
    }

    @Override
    public void onNotifiClicked() {
    }

    /*class NotificationReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Constant.ACTION_PREVIOUS:
                    previousSong();
                    break;
                case Constant.ACTION_PLAY_NEXT:
                    nextSong(true);
                    break;
                case Constant.ACTION_PLAY_PAUSE: {
                    if (mMediaPlayer.isPlaying()) {
                        pauseSong();
                    } else {
                        continueSong();
                    }
                    break;
                }
            }
        }
    }*/
}
