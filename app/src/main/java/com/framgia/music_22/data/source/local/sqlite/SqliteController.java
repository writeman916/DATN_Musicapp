package com.framgia.music_22.data.source.local.sqlite;

import com.framgia.music_22.data.model.Song;
import java.util.List;

public interface SqliteController {
    List<Song> getListSongLocal();

    Song getSong(int id);
}
