package com.framgia.music_22.utils;

import android.support.annotation.IntDef;

@IntDef({ TypeTab.TAB_HOME, TypeTab.TAB_OFFLINE, TypeTab.TAB_ARTIST, TypeTab.TAB_GENRES })
public @interface TypeTab {
    int TAB_HOME = 0;
    int TAB_OFFLINE = 1;
    int TAB_ARTIST = 2;
    int TAB_GENRES = 3;
}
