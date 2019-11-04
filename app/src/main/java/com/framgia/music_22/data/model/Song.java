package com.framgia.music_22.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String mSongId;
    private String mTitle;
    private String mGenre;
    private String mUserId;
    private String mStreamUrl;
    private int mDuaration;
    private String mUri;
    private Artist mArtist;

    public Song() {

    }

    protected Song(Builder builder) {
        mSongId = builder.mSongId;
        mTitle = builder.mTitle;
        mGenre = builder.mGenre;
        mUserId = builder.mUserId;
        mStreamUrl = builder.mStreamUrl;
        mDuaration = builder.mDuaration;
        mUri = builder.mUri;
        mArtist = builder.mArtist;
    }

    public Song(String songId, String title, String genre, String userId, String streamUrl,
            int duaration, String uri, Artist artist) {
        mSongId = songId;
        mTitle = title;
        mGenre = genre;
        mUserId = userId;
        mStreamUrl = streamUrl;
        mDuaration = duaration;
        mUri = uri;
        mArtist = artist;
    }

    public String getSongId() {
        return mSongId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getGenre() {
        return mGenre;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public int getDuaration() {
        return mDuaration;
    }

    public String getUri() {
        return mUri;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public static Creator<Song> getCREATOR() {
        return CREATOR;
    }

    // Builder class
    public static class Builder {

        private String mSongId;
        private String mTitle;
        private String mUserId;
        private String mStreamUrl;
        private String mGenre;
        private int mDuaration;
        private String mUri;
        private Artist mArtist;

        public Builder() {

        }

        public Builder(String songId, String title, String genre, String userId, String streamUrl,
                int duaration, String uri, Artist artist) {
            mSongId = songId;
            mTitle = title;
            mGenre = genre;
            mUserId = userId;
            mStreamUrl = streamUrl;
            mDuaration = duaration;
            mUri = uri;
            mArtist = artist;
        }

        public Builder withSongId(String songId) {
            mSongId = songId;
            return this;
        }

        public Builder withTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder withGenre(String genre) {
            mGenre = genre;
            return this;
        }

        public Builder withUserId(String userId) {
            mUserId = userId;
            return this;
        }

        public Builder withStreamUrl(String streamUrl) {
            mStreamUrl = streamUrl;
            return this;
        }

        public Builder withDuaration(int duaration) {
            mDuaration = duaration;
            return this;
        }

        public Builder withUri(String uri) {
            mUri = uri;
            return this;
        }

        public Builder withArtist(Artist artist) {
            mArtist = artist;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSongId);
        parcel.writeString(mTitle);
        parcel.writeString(mGenre);
        parcel.writeString(mUserId);
        parcel.writeString(mStreamUrl);
        parcel.writeInt(mDuaration);
        parcel.writeString(mUri);
        parcel.writeParcelable(mArtist, i);
    }

    protected Song(Parcel in) {
        mSongId = in.readString();
        mTitle = in.readString();
        mGenre = in.readString();
        mUserId = in.readString();
        mStreamUrl = in.readString();
        mDuaration = in.readInt();
        mUri = in.readString();
        mArtist = in.readParcelable(Artist.class.getClassLoader());
    }

    public class APISongProperties {
        public static final String SONG_ID = "id";
        public static final String TITLE = "title";
        public static final String GENRE = "genre";
        public static final String USER_ID = "user_id";
        public static final String STREAM_URL = "stream_url";
        public static final String DUARATION = "duration";
        public static final String SONG_URI = "uri";
    }
}
