package com.framgia.music_22.screen.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.framgia.music_22.data.model.Artist;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.screen.base.BaseFragment;
import com.framgia.music_22.screen.music_player.PlayMusicFragment;
import com.framgia.music_22.screen.song_list.SongByGenreFragment;
import com.framgia.music_22.utils.ConnectionChecking;
import com.framgia.music_22.utils.Constant;
import com.framgia.music_22.utils.TypeGenre;
import com.framgia.music_22.utils.custom_view.GenreButton;
import com.framgia.vnnht.music_22.R;
import java.util.ArrayList;

public class HomeFragment extends BaseFragment
        implements HomePageContract.View, ViewPager.OnPageChangeListener, View.OnClickListener,
        HomeCallBack {

    public static final String TAG = "HomeFragment";
    private static int DELAY_TIME = 2000;

    private ViewPager viewPagerBanner;
    private Handler mHandler;
    private LinearLayout mLinearDots;
    private int mSlideCurrentPage = 0;
    private ConnectionChecking mConnectionChecking;
    SlidePagerAdapter slidePagerAdapter;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (slidePagerAdapter == null) return;
            int VIRTUAL_SLIDE_NUMBER = 100;
            if (mSlideCurrentPage == slidePagerAdapter.getCount() * VIRTUAL_SLIDE_NUMBER) {
                mSlideCurrentPage = 0;
            } else {
                mSlideCurrentPage = mSlideCurrentPage % slidePagerAdapter.getCount();
                mSlideCurrentPage++;
            }
            viewPagerBanner.setCurrentItem(mSlideCurrentPage, true);
            mHandler.postDelayed(this, DELAY_TIME);
        }
    };

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        viewPagerBanner = view.findViewById(R.id.viewpager_banner);
        mLinearDots = view.findViewById(R.id.linear_dots);
        GenreButton buttonAllAudio = view.findViewById(R.id.button_all_audios);
        GenreButton buttonAllSong = view.findViewById(R.id.button_all_song);
        GenreButton buttonAlternativeRock = view.findViewById(R.id.button_alternative_rock);
        GenreButton buttonAmbient = view.findViewById(R.id.button_ambient);
        GenreButton buttonClassic = view.findViewById(R.id.button_classic);
        GenreButton buttonElectronic = view.findViewById(R.id.button_electronic);
        mConnectionChecking = new ConnectionChecking(getContext().getApplicationContext());

        viewPagerBanner.setOnPageChangeListener(this);
        buttonAllAudio.setOnClickListener(this);
        buttonAllSong.setOnClickListener(this);
        buttonAlternativeRock.setOnClickListener(this);
        buttonAmbient.setOnClickListener(this);
        buttonClassic.setOnClickListener(this);
        buttonElectronic.setOnClickListener(this);

        mHandler = new Handler();
        slidePagerAdapter = new SlidePagerAdapter(getActivity(), this);
        viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mSlideCurrentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPagerBanner.setAdapter(slidePagerAdapter);
        onCreateDots(0);
    }

    public void onCreateDots(int currentPosition) {
        if (mLinearDots != null) {
            mLinearDots.removeAllViews();
        }
        ImageView[] imageDots = new ImageView[Constant.NUMBER_OF_BANNER];

        for (int i = 0; i < Constant.NUMBER_OF_BANNER; i++) {
            imageDots[i] = new ImageView(getActivity());
            if (i == currentPosition) {
                imageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.active_dots));
            } else {
                imageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.unactive_dots));
            }
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(Constant.MARGIN_LEFT_RIGHT_DOTS, 0,
                    Constant.MARGIN_LEFT_RIGHT_DOTS, 0);
            mLinearDots.addView(imageDots[i], layoutParams);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        onCreateDots(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View view) {
        if (mConnectionChecking.isNetworkConnection()) {
            switch (view.getId()) {
                case R.id.button_all_audios:
                    goToListFrag(TypeGenre.ALL_AUDIO);
                    break;
                case R.id.button_all_song:
                    goToListFrag(TypeGenre.ALL_MUSIC);
                    break;
                case R.id.button_alternative_rock:
                    goToListFrag(TypeGenre.ALTERNATIVEROCK);
                    break;
                case R.id.button_ambient:
                    goToListFrag(TypeGenre.AMBIENT);
                    break;
                case R.id.button_classic:
                    goToListFrag(TypeGenre.CLASSICAL);
                    break;
                case R.id.button_electronic:
                    goToListFrag(TypeGenre.ELECTRONIC);
            }
        } else {
            Toast.makeText(getContext(), R.string.text_connection_information, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void goToListFrag(String genre) {
        getMainActivity().addFragment(
                SongByGenreFragment.Companion.newInstance(Constant.GENRES_URL + genre, genre),
                false, SongByGenreFragment.TAG, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, DELAY_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onSlideClicked(int position) {
        getMainActivity().addFragment(
                PlayMusicFragment.getOnlineInstance(getFakedPageList(), position), true,
                PlayMusicFragment.TAG, false);
    }

    ArrayList<Song> getFakedPageList() {
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song("274388374", "Em Làm Gì Tối Nay", "page", "335469825",
                "https://api.soundcloud.com/tracks/183737184/stream", 245025,
                "https://api.soundcloud.com/tracks/274388374", new Artist("335469825", "Khac Viet",
                "https://i1.sndcdn.com/artworks-000172175691-7re150-large.jpg")));
        songs.add(new Song("258060", "Anh nhớ em", "page", "335469825",
                "https://api.soundcloud.com/tracks/82356661/stream", 258060,
                "https://api.soundcloud.com/tracks/344850376", new Artist("335469825", "Tuấn Hưng",
                "https://i1.sndcdn.com/avatars-000035057508-gvkwjx-large.jpg")));
        songs.add(new Song("253205285", "Con đường mưa", "page", "335469825",
                "https://api.soundcloud.com/tracks/253205285/stream", 297118,
                "https://api.soundcloud.com/tracks/344850376",
                new Artist("335469825", "Cao Thái Sơn",
                        "http://nhac.hay365.com/thumbs/240x240/source/picture/album/ConDuongMua"
                                + ".jpg")));
        songs.add(new Song("344850376", "Thu cuối", "page", "335469825",
                "https://api.soundcloud.com/tracks/59915507/stream", 289986,
                "https://api.soundcloud.com/tracks/344850376", new Artist("335469825", "Wanbi",
                "https://avatar-nct.nixcdn.com/playlist/2012/01/31/o0UNP0XmF8lM.jpg")));
        songs.add(new Song("344850376", "Tháng tư là lời nói dối của em", "page", "335469825",
                "https://api.soundcloud.com/tracks/344850376/stream", 310206,
                "https://api.soundcloud.com/tracks/344850376",
                new Artist("335469825", "Hằng BingBoong, Yanbi Mr.T",
                        "https://i1.sndcdn.com/artworks-000245116986-80c0yw-large.jpg")));
        songs.add(new Song("344850376", "Đoạn đường vắng", "page", "335469825",
                "https://api.soundcloud.com/tracks/147220242/stream", 189118,
                "https://api.soundcloud.com/tracks/344850376",
                new Artist("335469825", "Nhật Kim Anh",
                        "http://triphuc.com/wp-content/uploads/2014/04/unnamed-300x201.jpg")));
        return songs;
    }
}
