package com.pornhub.admin.vkgallery.presenters

import android.support.v4.app.FragmentActivity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.pornhub.admin.vkgallery.views.GalleryPreview
import com.pornhub.admin.vkgallery.views.PreviewActivity

@InjectViewState
class GalleryPreviewPresenter(internal var activity: FragmentActivity?) : MvpPresenter<GalleryPreview>() {

    /** Ссылка на родительскую активность */
    var previewActivity: PreviewActivity

    init {
        previewActivity = activity as PreviewActivity
        if (previewActivity.galleryInfo == null) {
            previewActivity.initAndStartGalleryInfoLoader()
        }
    }

    /**
     * Отобразить процесс загрузки
     */
    fun showLoadingProcess() {
        viewState.showLoadingProcess()
    }

    /**
     * Скрыть процесс загрузки
     */
    fun hideLoadingProcess() {
        viewState.hideLoadingProcess()
    }

    /**
     * Загрузка информации о пользовательской галереи
     */
    fun allPhotosRequest() {
        showLoadingProcess()
        previewActivity.initAndStartGalleryInfoLoader()
    }
}