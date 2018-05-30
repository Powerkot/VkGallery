package com.pornhub.admin.vkgallery.utils

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.pornhub.admin.vkgallery.R

/**
 * Утилиты для работы с SnackBar
 */
class SnackUtils {

    companion object {
        fun showSnackbar(activity: Activity, message: String) {
            val snackbar = Snackbar.make(activity.window.decorView.findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_LONG).setAction("Action", null)
            val textView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(activity, R.color.vk_white))
            snackbar.show()
        }
    }
}