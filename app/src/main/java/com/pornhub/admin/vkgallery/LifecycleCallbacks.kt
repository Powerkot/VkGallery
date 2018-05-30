package com.pornhub.admin.vkgallery

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.pornhub.admin.vkgallery.views.PreviewActivity

class LifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
        //TODO IF NECESSARY
    }

    override fun onActivityStarted(activity: Activity?) {
        if (activity is PreviewActivity) {
            initToolbar(activity, activity.getToolbarTitle(), activity.getToolbarSubtitle())
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        //TODO IF NECESSARY
    }

    override fun onActivityPaused(activity: Activity?) {
        //TODO IF NECESSARY
    }

    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {
        //TODO IF NECESSARY
    }

    override fun onActivityStopped(activity: Activity?) {
        //TODO IF NECESSARY
    }

    override fun onActivityDestroyed(activity: Activity?) {
        //TODO IF NECESSARY
    }

    /**
     * Инициализация тулбара
     */
    fun initToolbar(activity: AppCompatActivity, title: String?, subtitle: String?): Toolbar? {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            if (title != null) {
                toolbar.setTitle(title)
            }
            if (subtitle != null) {
                toolbar.setSubtitle(subtitle)
            }
        }
        return toolbar
    }
}