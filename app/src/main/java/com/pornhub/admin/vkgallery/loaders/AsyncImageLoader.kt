package com.pornhub.admin.vkgallery.loaders

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.AsyncTaskLoader
import com.pornhub.admin.vkgallery.Constans.Companion.KEY_URL
import com.pornhub.admin.vkgallery.rest.VkPhotosAPI

/*
* Лоадер для загрузки изображения по url
*/
class AsyncImageLoader(context: Context, val bundle: Bundle?) : AsyncTaskLoader<Bitmap>(context) {

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): Bitmap? {
        bundle?.let {
            if (it.containsKey(KEY_URL)) {
                return VkPhotosAPI.requestImage(it.getString(KEY_URL))
            } else {
                return null
            }
        }
        return null
    }
}