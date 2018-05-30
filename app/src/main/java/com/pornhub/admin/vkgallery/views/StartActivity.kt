package com.pornhub.admin.vkgallery.views

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import co.gofynd.gravityview.GravityView
import com.pornhub.admin.vkgallery.App
import com.pornhub.admin.vkgallery.R
import com.pornhub.admin.vkgallery.databinding.ActivityVkLoginBinding
import com.pornhub.admin.vkgallery.utils.SnackUtils
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError

/**
 * Необходимо разработать приложение для просмотра фотографий пользователя Vkontakte

Экраны:
Экран логина
Экран превью фотографий в виде сетки
Экран просмотра фотографии в полноэранном режиме с дополнительной информацией по фото

По клику на превью переходить в полноэкранный режим
В полноэкранном режиме должна быть возможность свайпа для просмотра предыдущей/следующей фотографии без перехода на экран превью.
Необходимо реализовать кэш фотографий без использования сторонних решений

Допускается использование библиотек для работы с http запросами
НЕ допускается использование библиотек для загрузки изображений(Например Picasso)
НЕ допускается использование сторонних библиотек для асинхронной работы.
 */
class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityVkLoginBinding
    /** Вью для бэкграунда активности */
    lateinit var gravityView: GravityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vk_login)
        gravityView = GravityView.getInstance(this).setImage(binding.bg, R.drawable.wallpaper);
    }

    override fun onResume() {
        super.onResume()
        if (gravityView.deviceSupported()) {
            gravityView.registerListener()
        }
    }

    override fun onStop() {
        super.onStop()
        if (gravityView.deviceSupported()) {
            gravityView.unRegisterListener()
        }
    }

    fun attemptLogin(v: View) {
        VKSdk.login(this, VKScope.PHOTOS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken?) {
                App.instance.accessToken = res?.accessToken
                App.instance.userId = res?.userId
                startActivity(Intent(this@StartActivity, PreviewActivity::class.java))
                overridePendingTransition(R.anim.enter_from_right, R.anim.hold)
            }

            override fun onError(error: VKError?) {
                SnackUtils.showSnackbar(this@StartActivity, error?.errorMessage ?: getString(R.string.authentication_error))
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}