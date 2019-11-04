package com.framgia.music_22.screen.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.framgia.vnnht.music_22.R

/**
 * Created by VanNhat on 14/03/2019.
 * phan.van.nhat@framgia.com
 */
class Navigator {
    enum class AnimationType(val type: Int) {
        NONE(0), RIGHT_TO_LEFT(1), LEFT_TO_RIGHT(2), BOTTOM_UP(3), TOP_DOWN(4), FADED(5)
    }

    // Activity Region

    fun startActivity(activity: Activity, clazz: Class<out Activity>, bundle: Bundle? = null,
        animation: AnimationType = AnimationType.NONE) {
        val intent = Intent(activity, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        activity.startActivity(intent)

    }

    fun pendingTransition(activity: Activity, animation: AnimationType) {
        when (animation) {
            AnimationType.BOTTOM_UP -> activity.overridePendingTransition(R.anim.slide_bottom_up,
                R.anim.no_change)
            AnimationType.TOP_DOWN -> activity.overridePendingTransition(R.anim.no_change,
                R.anim.no_change)
            else -> {
                //TODO Here animation here
            }
        }
    }

    fun startActivityForResult(activity: Activity, clazz: Class<out Activity>,
        bundle: Bundle? = null, requestCode: Int) {
        val intent = Intent(activity, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        activity.startActivityForResult(intent, requestCode)
    }

    fun startActivityForResult(fragment: Fragment, clazz: Class<out Activity>,
        bundle: Bundle? = null, requestCode: Int) {
        val intent = Intent(fragment.context, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        fragment.startActivityForResult(intent, requestCode)
    }

    fun showSnackBar(activity: Activity, message: String, action: String? = null,
        actionListener: View.OnClickListener? = null, duration: Int) {
        val snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, duration)
    }

    fun addFragment(fragmentManager: FragmentManager,
        fragment: Fragment, frameId: Int, addToBackStack: Boolean = false, tag: String? = null,
        bundle: Bundle? = null, haveAnimation: Boolean = false) {
        val transaction = fragmentManager.beginTransaction()
        if (haveAnimation)
            transaction.setCustomAnimations(R.anim.slide_bottom_up, R.anim.slide_top_down,
                R.anim.slide_bottom_up, R.anim.slide_top_down)
        if (fragment.arguments == null) {
            fragment.arguments = bundle
        }
        transaction.add(frameId, fragment, tag)
        commitTransaction(transaction, addToBackStack)
    }

    fun showFragment(fragmentManager: FragmentManager,
        fragment: Fragment, addToBackStack: Boolean = false, haveAnimation: Boolean = false) {
        val transaction = fragmentManager.beginTransaction()
        if (haveAnimation)
            transaction.setCustomAnimations(R.anim.slide_bottom_up, R.anim.slide_top_down,
                R.anim.slide_bottom_up, R.anim.slide_top_down)
        transaction.show(fragment)
        commitTransaction(transaction, addToBackStack)
    }

    fun hideFragment(fragmentManager: FragmentManager,
        fragment: Fragment, addToBackStack: Boolean = false, haveAnimation: Boolean = false) {
        val transaction = fragmentManager.beginTransaction()
        if (haveAnimation)
            transaction.setCustomAnimations(R.anim.slide_bottom_up, R.anim.slide_top_down,
                R.anim.slide_bottom_up, R.anim.slide_top_down)
        transaction.hide(fragment)
        commitTransaction(transaction, addToBackStack)
    }

    private fun commitTransaction(transaction: FragmentTransaction,
        addToBackStack: Boolean = false, transit: Int = FragmentTransaction.TRANSIT_NONE) {
        if (addToBackStack) transaction.addToBackStack(null)
        if (transit != -1) transaction.setTransition(transit)
        transaction.commit()
    }

    fun findFragment(activity: Activity, TAG: String): Fragment? {
        return (activity as FragmentActivity).supportFragmentManager.findFragmentByTag(TAG)
    }

    fun hideKeyBoard(activity: Activity) {
        val inputMethod = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
    }
}
