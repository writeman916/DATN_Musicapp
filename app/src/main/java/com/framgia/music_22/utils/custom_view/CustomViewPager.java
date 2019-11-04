package com.framgia.music_22.utils.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import com.framgia.vnnht.music_22.R;

/**
 * Created by VanNhat on 01/02/2019.
 * phan.van.nhat@framgia.com
 */
public class CustomViewPager extends ViewPager {

    private static int BACK_CYCLE_FROM_BEGINNING = 100;
    private static int DEFAULT_SCROLL_INTERVAL = 2000;
    private int offsetAmount;
    private Handler autoScrollHandle = new Handler();
    private Boolean autoScroll;
    private Boolean autoSmoothScroll;
    private long autoScrollInterval = 0;
    private Boolean hasAlreadyPosted;
    private int lastDiff = 0;
    private Context mContext;

    public CustomViewPager(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDeclare(attrs);
    }

    void initDeclare(AttributeSet attrs) {
        TypedArray arr = mContext.obtainStyledAttributes(attrs, R.styleable.CustomViewPager);
        autoScroll = arr.getBoolean(R.styleable.CustomViewPager_autoScroll, false);
        autoSmoothScroll = arr.getBoolean(R.styleable.CustomViewPager_autoSmoothScroll, false);
        autoScrollInterval =
                arr.getInt(R.styleable.CustomViewPager_autoScrollInterval, DEFAULT_SCROLL_INTERVAL);
        arr.recycle();
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                lastDiff = position - offsetAmount;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private int getOffsetAmount() {
        if (getAdapter().getClass() == CustomPagerAdapter.class) {
            CustomPagerAdapter customAdapter = (CustomPagerAdapter) getAdapter();

            return customAdapter.getRealCount() * BACK_CYCLE_FROM_BEGINNING;
        } else {
            return 0;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setCurrentItem(getCurrentItem() + 1, !autoSmoothScroll);
            autoScrollHandle.postDelayed(this, autoScrollInterval);
        }
    };

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            checkAndResumeAutoScroll();
        } else {
            checkAndStopAutoScroll();
        }
    }

    private void checkAndResumeAutoScroll() {
        if (!autoScroll && !autoSmoothScroll || hasAlreadyPosted || getAdapter().getCount() == 1) {
            return;
        }
        autoScrollHandle.postDelayed(mRunnable, autoScrollInterval);
        hasAlreadyPosted = true;
    }

    private void checkAndStopAutoScroll() {
        if (!autoScroll && !autoSmoothScroll || !hasAlreadyPosted) return;
        autoScrollHandle.removeCallbacksAndMessages(null);
        hasAlreadyPosted = false;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        setCurrentItem(0);
    }

    @Override
    public void setCurrentItem(int position, boolean smoothScroll) {
        if (getAdapter() == null) return;
        int tempPos = position;
        if (getAdapter().getCount() == 0) {
            super.setCurrentItem(tempPos, smoothScroll);
        }
    }
}
