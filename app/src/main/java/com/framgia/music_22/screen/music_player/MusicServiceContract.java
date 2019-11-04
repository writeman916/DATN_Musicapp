package com.framgia.music_22.screen.music_player;

import android.media.MediaPlayer;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;

public interface MusicServiceContract {
    void updateMediaToClient(MediaPlayer mediaPlayer);

    void onUpdateOnlineSongDetail(Song song);

    void onUpdateOfflineSongDetail(OfflineSong song);
}
