package com.framgia.music_22.screen.gernes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.framgia.music_22.screen.base.BaseFragment;
import com.framgia.music_22.screen.song_list.SongByGenreFragment;
import com.framgia.music_22.utils.ConnectionChecking;
import com.framgia.music_22.utils.Constant;
import com.framgia.music_22.utils.TypeGenre;
import com.framgia.vnnht.music_22.R;

public class GenreFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "GenreFragment";
    private static final String LINK_TO_CHARTS_TOP = "top";
    private static final String LINK_TO_CHARTS_TRENDING = "trending";

    private ConnectionChecking mConnectionChecking;

    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gernes, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ImageButton buttonTrending = view.findViewById(R.id.button_trending);
        ImageButton buttonHot = view.findViewById(R.id.button_hot);
        ImageButton buttonCountry = view.findViewById(R.id.button_country);
        ImageButton buttonDanceHall = view.findViewById(R.id.button_dancehall);
        ImageButton buttonDanceEdm = view.findViewById(R.id.button_danceedm);
        ImageButton buttonDeepHouse = view.findViewById(R.id.button_deephouse);
        ImageButton buttonDisco = view.findViewById(R.id.button_disco);
        ImageButton buttonPiano = view.findViewById(R.id.button_piano);
        ImageButton buttonPop = view.findViewById(R.id.button_pop);
        mConnectionChecking = new ConnectionChecking(getContext().getApplicationContext());

        buttonTrending.setOnClickListener(this);
        buttonHot.setOnClickListener(this);
        buttonCountry.setOnClickListener(this);
        buttonDanceHall.setOnClickListener(this);
        buttonDanceEdm.setOnClickListener(this);
        buttonDeepHouse.setOnClickListener(this);
        buttonDisco.setOnClickListener(this);
        buttonPiano.setOnClickListener(this);
        buttonPop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mConnectionChecking.isNetworkConnection()) {
            switch (view.getId()) {
                case R.id.button_trending:
                    break;
                case R.id.button_hot:
                    break;
                case R.id.button_country:
                    goToListFrag(TypeGenre.COUNTRY);
                    break;
                case R.id.button_dancehall:
                    goToListFrag(TypeGenre.DANCE_HALL);
                    break;
                case R.id.button_danceedm:
                    goToListFrag(TypeGenre.DANCE_EDM);
                    break;
                case R.id.button_deephouse:
                    goToListFrag(TypeGenre.DEEP_HOUSE);
                    break;
                case R.id.button_disco:
                    goToListFrag(TypeGenre.DISCO);
                    break;
                case R.id.button_piano:
                    goToListFrag(TypeGenre.PIANO);
                    break;
                case R.id.button_pop:
                    goToListFrag(TypeGenre.POP);
                    break;
            }
        } else {
            Toast.makeText(getContext(),
                getResources().getString(R.string.text_connection_information), Toast.LENGTH_SHORT)
                .show();
        }
    }

    public void goToListFrag(String genre) {
        getMainActivity().addFragment(
            SongByGenreFragment.Companion.newInstance(Constant.GENRES_URL + genre, genre), false,
            TAG, true);
    }
}
