package com.pornhub.admin.vkgallery.common.models

import com.google.gson.annotations.SerializedName

/**
 * Копия изображения в разных размерах
 */
class Size(
        /** URL копии */
        @SerializedName("url") val url: String,
        /** ширина в px */
        @SerializedName("width") val width: Int,
        /** высота в px */
        @SerializedName("height") val height: Int,
        /** тип копии */
        @SerializedName("type") val type: String)

/**
 * Лайки
 */
class Likes(
        /** информация о том, поставил ли лайк текущий пользователь */
        @SerializedName("user_likes") val userLikes: Int,
        /** общее количество лайков */
        @SerializedName("count") val count: Int)

/**
 * Объект, описывающий фотографию
 */
class Item(
        /** идентификатор фотографии */
        @SerializedName("id") val id: Int,
        /** идентификатор альбома, в котором находится фотография */
        @SerializedName("album_id") val albumId: Int,
        /** идентификатор владельца фотографии */
        @SerializedName("owner_id") val ownerId: Int,
        /** массив с копиями изображения в разных размерах */
        @SerializedName("sizes") val sizes: List<Size>,
        /** текст описания фотографии */
        @SerializedName("text") val text: String,
        /** дата добавления в формате Unixtime */
        @SerializedName("date") val date: Long,
        @SerializedName("likes") val likes: Likes,
        @SerializedName("real_offset") val realOffset: Int,
        @SerializedName("post_id") val postId: Int,
        @SerializedName("has_filter") val hasFilter: Int
)

class PhotosResponse(@SerializedName("count") val count: Int,
                     @SerializedName("items") val items: List<Item>,
                     @SerializedName("more") val more: Int)

class GalleryInfo(@SerializedName("response") val response: PhotosResponse)