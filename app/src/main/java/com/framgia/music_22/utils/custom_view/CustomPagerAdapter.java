package com.framgia.music_22.utils.custom_view;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by VanNhat on 01/02/2019.
 * phan.van.nhat@framgia.com
 */
public class CustomPagerAdapter extends PagerAdapter {

    private PagerAdapter mAdapter;
    private int realCount;

    public CustomPagerAdapter(PagerAdapter pagerAdapter) {
        mAdapter = pagerAdapter;
    }

    public void setRealCount(int realCount) {
        this.realCount = realCount;
    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    @Override
    public int getCount() {
        switch (realCount) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return Integer.MAX_VALUE;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int virtualPosition;
        if (realCount == 0) {
            virtualPosition = realCount;
        } else {
            virtualPosition = position % realCount;
        }
        return mAdapter.instantiateItem(container, virtualPosition);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        int virtualPosition;
        if (realCount == 0) {
            virtualPosition = realCount;
        } else {
            virtualPosition = position % realCount;
        }
        mAdapter.destroyItem(container, virtualPosition, object);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        mAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
        mAdapter.restoreState(state, loader);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mAdapter.getPageTitle(position);
    }

    @Override
    public float getPageWidth(int position) {
        return mAdapter.getPageWidth(position);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

    @Override
    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return mAdapter.getItemPosition(object);
    }
}
