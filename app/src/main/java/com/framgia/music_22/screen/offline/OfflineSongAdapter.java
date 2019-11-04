package com.framgia.music_22.screen.offline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.screen.song_list.OnItemClickListener;
import com.framgia.vnnht.music_22.R;
import java.util.ArrayList;
import java.util.List;

public class OfflineSongAdapter extends RecyclerView.Adapter<OfflineSongAdapter.ViewHolder> {

    private Context mContext;
    private List<OfflineSong> mSongList;
    private OnItemClickListener mOnItemClickListener;

    OfflineSongAdapter(Context context) {
        mContext = context;
        mSongList = new ArrayList<>();
    }

    public void updateSongList(List<OfflineSong> songList) {
        if (mSongList != null) {
            mSongList.clear();
        }
        mSongList = songList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        return new ViewHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mSongList != null ? mSongList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextTitle, mTextArtist;
        private ImageView mImageAvatar;
        private ConstraintLayout mConstraintLayout;

        ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mImageAvatar = itemView.findViewById(R.id.image_avatar);
            mConstraintLayout = itemView.findViewById(R.id.constrain_row);
            mOnItemClickListener = onItemClickListener;
            mConstraintLayout.setOnClickListener(this);
        }

        void getViewHolder(ViewHolder holder, int position) {
            Glide.with(mContext).load(R.drawable.image_offline_music).into(mImageAvatar);
            holder.mTextTitle.setText(mSongList.get(position).getTitle());
            holder.mTextArtist.setText(mSongList.get(position).getArtistName());
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
