package com.framgia.music_22.utils;

import com.framgia.vnnht.music_22.BuildConfig;

public class Constant {

    public static final int TAB_COUNT = 4;

    public static final int NUMBER_OF_BANNER = 6;
    public static final int MARGIN_LEFT_RIGHT_DOTS = 4;
    public static final String BANNER_ITEM1 =
            "https://i.ytimg.com/vi/U5ruMh6_Jwk/maxresdefault.jpg";
    public static final String BANNER_ITEM2 =
            "https://i.ytimg.com/vi/M8o6VioxY6s/maxresdefault.jpg";
    public static final String BANNER_ITEM3 =
            "https://img.youtube.com/vi/omM5XAN6m70/mqdefault.jpg";
    public static final String BANNER_ITEM4 = "http://s.nhac.vn/v1/seo/song/s1/0/0/463/474207.jpg";
    public static final String BANNER_ITEM5 =
            "https://i.ytimg.com/vi/UCXao7aTDQM/maxresdefault.jpg";
    public static final String BANNER_ITEM6 =
            "http://img.hplus.com.vn/460x260/poster/2015/04/21/194089-DOAN_DUONG_VANG.jpg";

    // For get data from api
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECT_TIMEOUT = 15000;
    public static final String BASE_URL = "http://api.soundcloud.com";
    public static final String CLIENT_ID = "?client_id=" + BuildConfig.API_KEY;
    public static final String GENRES_URL =
            BASE_URL + "/tracks" + CLIENT_ID + "&linked_partitioning=1&genres=";
    public static final String CLIENT_ID_V2 = "&client_id=" + BuildConfig.API_KEY;
            // for next pull request
    public static final String GENRES_URL_V2 = "https://api-v2.soundcloud.com/charts?kind=";
            // for next pull request
    public static final String LIMIT_NUMBER = "&limit=51";
    public static final String ARRAY_JSON_NAME = "collection";
    public static final String NEXT_HREF = "next_href";

    // INTENT ACTION
    public static final String GO_TO_PLAYER = "com.framgia.vnnht.music_22.GO_TO_PLAYER";
    public static final String ACTION_PREVIOUS = "com.framgia.vnnht.music_22.ACTION_PREVIOUS";
    public static final String ACTION_PLAY_PAUSE = "com.framgia.vnnht.music_22.ACTION_PLAY_PAUSE";
    public static final String ACTION_PLAY_NEXT = "com.framgia.vnnht.music_22.ACTION_PLAY_NEXT";

    public static final String PREVIOUS = "PREVIOUS";
    public static final String PAUSE = "PAUSE";
    public static final String NEXT = "NEXT";
}
