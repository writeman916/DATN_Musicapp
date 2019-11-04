package com.framgia.music_22.utils.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.framgia.vnnht.music_22.R
import kotlinx.android.synthetic.main.layout_genre_button.view.*


class GenreButton @JvmOverloads constructor(context: Context, attrs: AttributeSet,
    defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

  var tvGenreName: String? = null
    set(value) {
      tv_name.text = value
      field = value
    }

  var tvNumber: Int? = null
    set(value) {
      tv_number.text = context.getString(R.string.video_number, value)
      field = value
    }

  var ivGenreImg: Drawable? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(value) {
      value?.let { iv_genre.setImageDrawable(it) }
      field = value
    }

  init {
    initView(attrs)
  }

  @SuppressLint("Recycle")
  private fun initView(attrs: AttributeSet) {
    LayoutInflater.from(context).inflate(R.layout.layout_genre_button, this, true)
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GenreButton)

    try {
      tvGenreName = typedArray.getString(R.styleable.GenreButton_genre_name)
      tvNumber = typedArray.getInt(R.styleable.GenreButton_video_number, 0)
      ivGenreImg = typedArray.getDrawable(R.styleable.GenreButton_genre_avatar)
    } finally {
      typedArray.recycle()
    }
  }
}
