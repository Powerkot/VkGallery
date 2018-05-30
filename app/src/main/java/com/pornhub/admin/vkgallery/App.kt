package com.pornhub.admin.vkgallery

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import java.util.*

/**
 * Класс приложения
 */
class App : Application() {

    /** Gson serializer */
    var gson: Gson? = null
        get() {
            if (field == null) {
                field = GsonBuilder()
                        .setDateFormat("dd.MM.yyyy")
                        .create()
            }
            return field
        }

    /** Токен ВК*/
    var accessToken: String? = null
    /** Идентификатор пользователя */
    var userId: String? = null
    /**
     * Кэш для миниатюр
     * Ключ - id изображения, значение - изображение
     */
    var lruCacheForMinImgSizes: LruCache<Int, Bitmap>? = null
    /**
     * Кэш для оригиналов
     * Ключ - id изображения, значение - изображение
     */
    var lruCacheForMaxImgSizes: LruCache<Int, Bitmap>? = null
    /**
     * Хранилище идентификатора изображения и списка url адресов,
     * по которым можно скачать изображение нужного размера
     */
    var mapImageIdToTwoUrls: HashMap<Int, ArrayList<String>>? = null

    private val vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            if (newToken == null) {
                //TODO: VKAccessToken is invalid
            }
        }
    }

    companion object {

        /** Экземпляр приложения */
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(LifecycleCallbacks())
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)
    }
}
