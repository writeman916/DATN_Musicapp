package com.framgia.music_22.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {

    private String mSingerId;
    private String mAvatarUrl;
    private String mSingerName;

    public Artist(String singerId, String singerName, String avatarUrl) {
        mSingerId = singerId;
        mAvatarUrl = avatarUrl;
        mSingerName = singerName;
    }

    public String getSingerId() {
        return mSingerId;
    }

    public void setSingerId(String singerId) {
        mSingerId = singerId;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public String getSingerName() {
        return mSingerName;
    }

    public void setSingerName(String singerName) {
        mSingerName = singerName;
    }

    public static Creator<Artist> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSingerId);
        parcel.writeString(mAvatarUrl);
        parcel.writeString(mSingerName);
    }

    protected Artist(Parcel in) {
        mSingerId = in.readString();
        mAvatarUrl = in.readString();
        mSingerName = in.readString();
    }

    public class APIArtistProperties {
        public static final String USER_ID = "id";
        public static final String USER_NAME = "username";
        public static final String AVATAR_URL = "avatar_url";
    }
}
