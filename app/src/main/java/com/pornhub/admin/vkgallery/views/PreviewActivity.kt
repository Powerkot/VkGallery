package com.pornhub.admin.vkgallery.views

import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.util.LruCache
import com.arellomobile.mvp.MvpAppCompatActivity
import com.pornhub.admin.vkgallery.App
import com.pornhub.admin.vkgallery.Constans
import com.pornhub.admin.vkgallery.Constans.Companion.GALLERY_INFO_LOADER_ID
import com.pornhub.admin.vkgallery.R
import com.pornhub.admin.vkgallery.common.models.GalleryInfo
import com.pornhub.admin.vkgallery.loaders.AsyncGalleryInfoLoader
import com.pornhub.admin.vkgallery.loaders.AsyncImageLoader
import java.util.HashMap

class PreviewActivity : MvpAppCompatActivity(), ActivityToolbar, LoaderManager.LoaderCallbacks<Any> {

    /** Информация о содержимом галереи */
    var galleryInfo: GalleryInfo? = null

    companion object {
        /** Ключ для хранения информации о содержимом галереи при перевороте */
        val EXTRA_GALLERY_INFO = "exampleJson"
        /** Пустое изображение для первичной инициализации кэша */
        val emptyBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_toolbar)
        replaceFragment(GalleryPreviewFragment.newInstance(), R.id.frame)
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_GALLERY_INFO)) {
            galleryInfo = App.instance.gson?.fromJson(savedInstanceState.getString(EXTRA_GALLERY_INFO), GalleryInfo::class.java)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (galleryInfo != null) {
            outState?.putString(EXTRA_GALLERY_INFO, App.instance.gson?.toJson(galleryInfo))
        }
    }

    /**
     * Заменить фрагмент
     * @param fragment        новый фрагмент
     * @param containerViewId ид контейнера
     */
    fun replaceFragment(fragment: Fragment, @IdRes containerViewId: Int) {
        supportFragmentManager.beginTransaction().replace(containerViewId, fragment, fragment.javaClass.name).commit()
    }

    override fun getToolbarTitle(): String = "Фотогалерея пользователя"

    override fun getToolbarSubtitle(): String? = null

    /**
     * Инициализировать и запустить лоадер для получения информации о содержимом галереи
     */
    fun initAndStartGalleryInfoLoader() {
        val bundle = Bundle()
        bundle.putString(Constans.KEY_OWNER_ID, App.instance.userId)
        bundle.putString(Constans.KEY_V, Constans.vkApiVersion)
        bundle.putString(Constans.KEY_ACCESS_TOKEN, App.instance.accessToken)
        makeOperationAddNumber(Constans.GALLERY_INFO_LOADER_ID, bundle)
    }

    /**
     * Инициализировать и запустить лоадер для для загрузки изображения по url
     */
    fun initAndStartImageLoader(id: Int, isMiniature: Boolean) {
        val bundle = Bundle()
        var newId = id
        var url: String?
        if (isMiniature) {
            url = App.instance.mapImageIdToTwoUrls?.get(id)?.get(0)
        } else {
            newId = changeIdForBigSizeImage(id)
            url = App.instance.mapImageIdToTwoUrls?.get(id)?.get(1)
        }
        bundle.putString(Constans.KEY_URL, url)
        makeOperationAddNumber(newId, bundle)
    }

    /**
     *
     */
    fun changeIdForBigSizeImage(id: Int): Int {
        return id
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Any> {
        if (id == GALLERY_INFO_LOADER_ID) {
            return AsyncGalleryInfoLoader(this, args) as Loader<Any>
        } else {
            return AsyncImageLoader(this, args) as Loader<Any>
        }
    }

    override fun onLoadFinished(loader: Loader<Any>, data: Any?) {
        if (data is GalleryInfo) {
            galleryInfo = data as GalleryInfo?
            if (App.instance.lruCacheForMinImgSizes == null ||
                    App.instance.lruCacheForMaxImgSizes == null) {
                initLruCaches()
                fillLruCaches()
                val fragment = supportFragmentManager.findFragmentByTag("com.pornhub.admin.vkgallery.views.GalleryPreviewFragment")
                if (fragment != null && fragment is GalleryPreviewFragment) {
                    (fragment as GalleryPreviewFragment).initAdapter()
                }
            }
        } else if (data is Bitmap) {
            if (App.instance.lruCacheForMinImgSizes != null) {
                App.instance.lruCacheForMinImgSizes?.put(loader.id, data)
                val fragment = supportFragmentManager.findFragmentByTag("com.pornhub.admin.vkgallery.views.GalleryPreviewFragment")
                if (fragment != null && fragment is GalleryPreviewFragment) {
                    (fragment as GalleryPreviewFragment).refreshAdapter()
                }
            }
        }
    }

    fun initLruCaches() {
        val maxMemory = Runtime.getRuntime().maxMemory()
        val cacheSize = maxMemory / 8
        App.instance.lruCacheForMinImgSizes = object : LruCache<Int, Bitmap>(cacheSize.toInt()) {
            override fun sizeOf(key: Int, bitmap: Bitmap): Int {
                if (!bitmap.equals(emptyBitmap)) {
                    return bitmap.getByteCount()
                } else {
                    return 0
                }
            }
        }
        App.instance.lruCacheForMaxImgSizes = object : LruCache<Int, Bitmap>(cacheSize.toInt()) {
            override fun sizeOf(key: Int, bitmap: Bitmap): Int {
                if (!bitmap.equals(emptyBitmap)) {
                    return bitmap.getByteCount()
                } else {
                    return 0
                }
            }
        }
    }

    fun fillLruCaches() {
        App.instance.mapImageIdToTwoUrls = HashMap()
        galleryInfo?.response?.items?.let {
            for (item in it) {
                App.instance.lruCacheForMinImgSizes?.put(item.id, emptyBitmap)
                App.instance.lruCacheForMaxImgSizes?.put(item.id, emptyBitmap)
                val urlsArray = ArrayList<String>()
                urlsArray.add(item.sizes.get(0).url) //Первый (минимальный размер изображения)
                urlsArray.add(item.sizes.get(item.sizes.size - 1).url) //Последний (минимальный размер изображения)
                App.instance.mapImageIdToTwoUrls?.put(item.id, urlsArray)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Any>) {
        //TODO
    }

    private fun makeOperationAddNumber(id: Int, bundle: Bundle) {
        val loader: Loader<Long>? = supportLoaderManager.getLoader(id)
        if (loader == null) {
            supportLoaderManager.initLoader(id, bundle, this)
        } else {
            supportLoaderManager.restartLoader(id, bundle, this)
        }
    }
}