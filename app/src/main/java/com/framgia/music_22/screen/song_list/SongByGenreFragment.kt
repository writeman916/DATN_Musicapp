package com.framgia.music_22.screen.song_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.framgia.music_22.data.model.MoreSong
import com.framgia.music_22.data.model.Song
import com.framgia.music_22.data.repository.SongRepository
import com.framgia.music_22.data.source.local.SongLocalDataSource
import com.framgia.music_22.data.source.remote.SongRemoteDataSource
import com.framgia.music_22.screen.base.BaseFragment
import com.framgia.music_22.screen.music_player.PlayMusicFragment
import com.framgia.vnnht.music_22.R
import kotlinx.android.synthetic.main.fragment_song_by_genre.*

/**
 * Created by VanNhat on 13/03/2019.
 * phan.van.nhat@framgia.com
 */

class SongByGenreFragment : BaseFragment(), OnItemClickListener, SongByGenreContract.View {

    private var mAdapter: SongByGenreAdapter? = null
    private var mSongList: List<Song>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_song_by_genre, container, false)
    }

    override fun initView() {
        super.initView()
        recycler_songs_by_genre.setHasFixedSize(true)
        mAdapter = SongByGenreAdapter(requireContext())
        mAdapter?.setOnItemClickListener(this)
        recycler_songs_by_genre.adapter = mAdapter
        btn_back.setOnClickListener {
            getMainActivity().onBackPressed()
        }
    }

    override fun initData() {
        super.initData()
        val genreLink = arguments?.getString(EXTRA_GENRE_LINK)
        val genre = arguments?.getString(EXTRA_GENRE_NAME)
        tv_genre.text = genre
        val songRepository = SongRepository.getsInstance(SongRemoteDataSource.getInstance(),
            SongLocalDataSource.getInstance(requireContext().applicationContext))
        val presenter = SongByGenrePresenter(this, songRepository)
        presenter.getSongByGenres(genreLink)
    }

    override fun onGetSongByGenreSuccess(moreSong: MoreSong?) {
        pr_loading.visibility = View.GONE
        mSongList = moreSong?.songsList
        mAdapter?.updateSongList(mSongList)
    }

    override fun onError(ex: Exception?) {
        pr_loading.visibility = View.GONE
        Toast.makeText(requireContext(), ex?.message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int) {
        getMainActivity().addFragment(PlayMusicFragment.getOnlineInstance(mSongList, position),
            true, PlayMusicFragment.TAG, false)
    }

    companion object {
        private const val EXTRA_GENRE_LINK = "EXTRA_GENRE_LINK"
        private const val EXTRA_GENRE_NAME = "EXTRA_GENRE_NAME"

        const val TAG = "SongByGenreFragment"

        fun newInstance(genreLink: String, genreName: String) = SongByGenreFragment().apply {
            val bundle = Bundle()
            bundle.putString(EXTRA_GENRE_LINK, genreLink)
            bundle.putString(EXTRA_GENRE_NAME, genreName)
            arguments = bundle
        }
    }
}
