package com.pornhub.admin.vkgallery.views

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.pornhub.admin.vkgallery.App
import com.pornhub.admin.vkgallery.R
import com.pornhub.admin.vkgallery.adapters.PhotosRecyclerAdapter
import com.pornhub.admin.vkgallery.presenters.GalleryPreviewPresenter

/**
 * Фрагмент, отображающий пользовательскую галерею фотографий
 */
class GalleryPreviewFragment: MvpAppCompatFragment(), GalleryPreview {

    /** Презентер */
    lateinit var galleryPreviewPresenter: GalleryPreviewPresenter
    /** Обновление по свайпу вниз */
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    /** Сообщение об ошибке */
    private lateinit var textViewErrorMessage: View
    /** Ресайклер */
    private lateinit var recyclerViewPhoto: RecyclerView

    companion object {
        /** Инстанс */
        fun newInstance(): GalleryPreviewFragment = GalleryPreviewFragment()
        /** Количество колонок изображений */
        private val COUNT_COLUMNS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryPreviewPresenter = GalleryPreviewPresenter(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        initViews()
        assignListeners()
    }

    /**
     * Получаем вьюхи из разметки
     * @param view корневой элемент разметки
     */
    private fun findViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.layout_swipe_refresh)
        textViewErrorMessage = view.findViewById<View>(R.id.tv_error_message)
        recyclerViewPhoto = view.findViewById<RecyclerView>(R.id.rv_photo)
    }

    /**
     * Первичная инициализация вьюх
     */
    private fun initViews() {
        recyclerViewPhoto.layoutManager = GridLayoutManager(activity, COUNT_COLUMNS)
        initAdapter()
    }

    /**
     * Первичная инициализация адаптера
     */
    fun initAdapter() {
        if (App.instance.lruCacheForMinImgSizes != null) {
            recyclerViewPhoto.adapter = PhotosRecyclerAdapter(activity!!, App.instance.lruCacheForMinImgSizes!!)
        }
        hideLoadingProcess()
    }

    /**
     * Привязка слушателей
     */
    private fun assignListeners() {
        swipeRefreshLayout.setOnRefreshListener { galleryPreviewPresenter.allPhotosRequest() }
    }

    override fun refreshAdapter() {
        recyclerViewPhoto.adapter?.notifyDataSetChanged()
    }

    override fun showLoadingProcess() {
        hideErrorMessage()
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingProcess() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showErrorMessage() {
        textViewErrorMessage.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        textViewErrorMessage.visibility = View.GONE
    }
}