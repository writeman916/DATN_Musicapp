package com.framgia.music_22.screen.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.framgia.music_22.screen.main.MainActivity

/**
 * Created by VanNhat on 13/03/2019.
 * phan.van.nhat@framgia.com
 */
abstract class BaseFragment : Fragment() {
    private var showHideToolBarListener: OnShowHideToolBarListener? = null

    override fun onAttach(context: Context?) {
        if (context is OnShowHideToolBarListener) {
            showHideToolBarListener = context
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showHideToolBarListener?.hideToolBar(shouldHideToolBar())
        initView()
        initData()
        handleEvent()
    }

    fun getMainActivity(): MainActivity {
        return requireActivity() as MainActivity
    }

    fun hideBottomButton() {
        getMainActivity().hideBottomButton()
    }

    fun showBottomButton() {
        getMainActivity().showBottomButton()
    }

    open fun handleEvent() {}

    open fun initView() {}

    open fun initData() {}

    open fun shouldHideToolBar(): Boolean {
        return false
    }
}
