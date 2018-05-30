package com.pornhub.admin.vkgallery.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface GalleryPreview : MvpView {

    /** Отобразить сообщение об ошибке */
    fun showErrorMessage()
    /** Скрыть сообщение об ошибке */
    fun hideErrorMessage()
    /** Отобразить процесс загрузки */
    fun showLoadingProcess()
    /** Скрыть процесс загрузки */
    fun hideLoadingProcess()
    /** Запрос обновления данных в адаптере */
    fun refreshAdapter()
}