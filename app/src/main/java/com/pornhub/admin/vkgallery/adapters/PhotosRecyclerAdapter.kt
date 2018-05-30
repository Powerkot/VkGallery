package com.pornhub.admin.vkgallery.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.pornhub.admin.vkgallery.R
import com.pornhub.admin.vkgallery.views.PreviewActivity

class PhotosRecyclerAdapter(context: Context, lruCache: LruCache<Int, Bitmap>) : RecyclerView.Adapter<ViewHolder>() {

    /** Ссылка на контекст родительской активности */
    private val context = context
    /**
     * Кэш для миниатюр
     * Ключ - id изображения, значение - изображение
     */
    private val lruCache = lruCache
    /** Мэпа для сопоставления позиции холдера с идентификатором изображения в кэше*/
    private val mapPositionToLruCacheKey: HashMap<Int, Int>

    init {
        mapPositionToLruCacheKey = HashMap()
        var i = 0
        for (key in lruCache.snapshot().keys) {
            mapPositionToLruCacheKey.put(i, key)
            i++
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lruCache.putCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        clearAllForReuse(holder)
        val lruCacheKey = mapPositionToLruCacheKey.get(position)
        if (lruCacheKey != null) {
            changeImageViewState(lruCache.get(lruCacheKey), holder)
            if (lruCache.get(lruCacheKey).equals(PreviewActivity.emptyBitmap)) {
                requestImageLoad(lruCacheKey)
            }
        }
    }

    /**
     * Обнуление вьюх в холдере
     * @param holder холдер
     */
    fun clearAllForReuse(holder: ViewHolder) {
        holder.imageView.setImageBitmap(null)
        holder.progressBar.visibility = View.VISIBLE
    }

    /**
     * Изменение вьюх в холдере по состоянию загрузки изображений
     * @param bitmap изображение
     * @param holder холдер
     */
    fun changeImageViewState(bitmap: Bitmap, holder: ViewHolder) {
        if (bitmap.equals(PreviewActivity.emptyBitmap)) {
            holder.progressBar.visibility = View.VISIBLE
        } else {
            holder.progressBar.visibility = View.GONE
            holder.imageView.setImageBitmap(bitmap)
        }
    }

    /**
     * Инициализировать и запустить лоадер для для загрузки изображения
     * @param id идентификатор изображения в кэше
     */
    fun requestImageLoad(id: Int) {
        (context as PreviewActivity).initAndStartImageLoader(id, true)
    }
}

class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    val imageView: ImageView
    val progressBar: ProgressBar

    init {
        imageView = v.findViewById(R.id.image_photo)
        progressBar = v.findViewById(R.id.progress)
    }
}