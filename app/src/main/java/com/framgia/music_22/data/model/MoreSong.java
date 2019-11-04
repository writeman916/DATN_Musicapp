package com.framgia.music_22.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class MoreSong implements Parcelable {
    private List<Song> mSongsList;
    private String mNextHref;

    public MoreSong() {
    }

    public MoreSong(List<Song> songList, String nextHref) {
        mSongsList = songList;
        mNextHref = nextHref;
    }

    private MoreSong(Parcel in) {
        mSongsList = in.createTypedArrayList(Song.CREATOR);
        mNextHref = in.readString();
    }

    public static final Creator<MoreSong> CREATOR = new Creator<MoreSong>() {
        @Override
        public MoreSong createFromParcel(Parcel in) {
            return new MoreSong(in);
        }

        @Override
        public MoreSong[] newArray(int size) {
            return new MoreSong[size];
        }
    };

    public List<Song> getSongsList() {
        return mSongsList;
    }

    public void setSongsList(List<Song> songsList) {
        mSongsList = songsList;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        mNextHref = nextHref;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mSongsList);
        parcel.writeString(mNextHref);
    }
}
