package com.framgia.music_22.screen.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.framgia.music_22.utils.Constant

/**
 * Created by VanNhat on 03/04/2019.
 * phan.van.nhat@framgia.com
 */
class NotificationReceiver(
    private val notificationCallBack: NotificationCallBack) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constant.ACTION_PREVIOUS -> notificationCallBack.onBackClicked()


            Constant.ACTION_PLAY_NEXT -> notificationCallBack.onNextClicked()


            Constant.ACTION_PLAY_PAUSE -> notificationCallBack.onPauseClicked()


            Constant.GO_TO_PLAYER -> notificationCallBack.onNotifiClicked()
        }
    }
}