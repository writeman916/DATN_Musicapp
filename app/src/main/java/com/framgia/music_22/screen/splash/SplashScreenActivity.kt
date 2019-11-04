package com.framgia.music_22.screen.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.framgia.music_22.screen.main.MainActivity
import com.framgia.vnnht.music_22.R


class SplashScreenActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)
    Handler().postDelayed({
      startActivity(Intent(this, MainActivity::class.java))
      this.finish()
    }, DELAYED_TIME)
  }

  companion object {
    private const val DELAYED_TIME = 1500L
  }
}
