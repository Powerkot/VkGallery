package com.pornhub.admin.vkgallery.rest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request


class VkPhotosAPI {

    companion object {
        val BASE_URL = "https://api.vk.com/method/"

        fun getAll(ownerId: String?, v: String, accessToken: String?): String? {
            val httpBuider = HttpUrl.parse(BASE_URL + "photos.getAll")
                    ?.newBuilder()
                    ?.addQueryParameter("owner_id", ownerId)
                    ?.addQueryParameter("v", v)
                    ?.addQueryParameter("access_token", accessToken)
            val response = httpBuider?.let {
                OkHttpClient()
                        .newCall(
                                Request.Builder()
                                        .url(it.build())
                                        .get()
                                        .build()
                        ).execute()
            }
            return response?.body()?.string()
        }

        fun requestImage(url: String): Bitmap {
            val response = OkHttpClient()
                    .newCall(
                            Request.Builder()
                                    .url(url)
                                    .get()
                                    .build()
                    ).execute()

            val inputStream = response.body()?.byteStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            return bitmap
        }
    }
}