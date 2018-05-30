package com.pornhub.admin.vkgallery.loaders

import android.content.Context
import android.os.Bundle
import android.support.v4.content.AsyncTaskLoader
import com.pornhub.admin.vkgallery.App
import com.pornhub.admin.vkgallery.Constans
import com.pornhub.admin.vkgallery.common.models.GalleryInfo
import com.pornhub.admin.vkgallery.rest.VkPhotosAPI

/**
 * Лоадер для получения информации о содержимом галереи
 */
class AsyncGalleryInfoLoader(context: Context, val bundle: Bundle?) : AsyncTaskLoader<GalleryInfo>(context) {

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): GalleryInfo? {
        bundle?.let {
            if (
                    it.containsKey(Constans.KEY_OWNER_ID) &&
                    it.containsKey(Constans.KEY_V) &&
                    it.containsKey(Constans.KEY_ACCESS_TOKEN)) {
                val responseJson = VkPhotosAPI.getAll(
                        it.getString(Constans.KEY_OWNER_ID),
                        it.getString(Constans.KEY_V),
                        it.getString(Constans.KEY_ACCESS_TOKEN))
                return App.instance.gson?.fromJson<GalleryInfo>(responseJson, GalleryInfo::class.java)
            } else {
                return null
            }
        }
        return null
    }
}