package com.framgia.music_22.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OfflineSong implements Parcelable {

    private String mSongId;
    private String mTitle;
    private String mGenre;
    private String mSongPath;
    private String mArtistName;
    private int mDuaration;

    public OfflineSong() {
    }

    public OfflineSong(String songId, String title, String genre, String songPath,
            String artistName, int duaration) {
        mSongId = songId;
        mTitle = title;
        mGenre = genre;
        mSongPath = songPath;
        mArtistName = artistName;
        mDuaration = duaration;
    }

    protected OfflineSong(Builder builder) {
        mSongId = builder.mSongId;
        mTitle = builder.mTitle;
        mGenre = builder.mGenre;
        mSongPath = builder.mSongPath;
        mArtistName = builder.mArtistName;
        mDuaration = builder.mDuaration;
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

    public String getSongPath() {
        return mSongPath;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public int getDuaration() {
        return mDuaration;
    }

    public static Creator<OfflineSong> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<OfflineSong> CREATOR = new Creator<OfflineSong>() {
        @Override
        public OfflineSong createFromParcel(Parcel in) {
            return new OfflineSong(in);
        }

        @Override
        public OfflineSong[] newArray(int size) {
            return new OfflineSong[size];
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
        parcel.writeString(mSongPath);
        parcel.writeString(mArtistName);
        parcel.writeInt(mDuaration);
    }

    protected OfflineSong(Parcel in) {
        mSongId = in.readString();
        mTitle = in.readString();
        mGenre = in.readString();
        mSongPath = in.readString();
        mArtistName = in.readString();
        mDuaration = in.readInt();
    }

    public static class Builder {
        private String mSongId;
        private String mTitle;
        private String mGenre;
        private String mSongPath;
        private String mArtistName;
        private int mDuaration;

        public Builder() {
        }

        public Builder(String songId, String title, String genre, String songPath,
                String artistName, int duaration) {
            mSongId = songId;
            mTitle = title;
            mGenre = genre;
            mSongPath = songPath;
            mArtistName = artistName;
            mDuaration = duaration;
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

        public Builder withSongPath(String songPath) {
            mSongPath = songPath;
            return this;
        }

        public Builder withDuaration(int duaration) {
            mDuaration = duaration;
            return this;
        }

        public Builder withAritstName(String aritstName) {
            mArtistName = aritstName;
            return this;
        }

        public OfflineSong build() {
            return new OfflineSong(this);
        }
    }
}
