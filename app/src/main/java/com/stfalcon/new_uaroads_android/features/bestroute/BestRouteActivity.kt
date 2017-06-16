/*
 * Copyright (c) 2017 stfalcon.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.stfalcon.new_uaroads_android.features.bestroute

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.WebViewClient
import com.stfalcon.mvphelper.MvpActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uaroads.osrmrouting.view.NavigatorActivity
import kotlinx.android.synthetic.main.activity_best_route.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class BestRouteActivity : MvpActivity<BestRouteContract.Presenter, BestRouteContract.View>(), BestRouteContract.View {

    override fun getLayoutResId() = R.layout.activity_best_route

    lateinit var locationFrom: LatLng
    lateinit var locationTo: LatLng
    lateinit var titleFrom: String
    lateinit var titleTo: String
    lateinit var rxPermissions: RxPermissions

    companion object {
        private val KEY_LOCATION_FROM = "location_from"
        private val KEY_LOCATION_TO = "location_to"
        private val KEY_TITLE_FROM = "title_from"
        private val KEY_TITLE_TO = "title_to"

        fun getIntent(context: Context, locationFrom: LatLng,
                      locationTo: LatLng, titleFrom: String, titleTo: String)
                = context.intentFor<BestRouteActivity>(
                KEY_LOCATION_FROM to locationFrom,
                KEY_LOCATION_TO to locationTo,
                KEY_TITLE_FROM to titleFrom,
                KEY_TITLE_TO to titleTo
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermissions = RxPermissions(this)
        initToolbar()
        initWebView()
        initViews()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animationView.showView()
            animationView.post {
                collapse(animationView, 300, toolbar.height)
            }
        }
    }

    override fun parseIntent() {
        super.parseIntent()
        locationFrom = intent.getSerializableExtra(KEY_LOCATION_FROM) as LatLng
        locationTo = intent.getSerializableExtra(KEY_LOCATION_TO) as LatLng
        titleFrom = intent.getSerializableExtra(KEY_TITLE_FROM) as String
        titleTo = intent.getSerializableExtra(KEY_TITLE_TO) as String
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { presenter?.onClickNavigateButton() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.setWebViewClient(WebViewClient())
    }

    private fun initViews() {
        btnGo.setOnClickListener { presenter?.onClickGoButton() }
    }

    override fun setFromToInfo(titleFrom: String, titleTo: String) {
        tvTitleFrom.text = titleFrom
        tvTitleTo.text = titleTo
    }

    override fun navigateBack() {
        finish()
    }

    override fun showMap(url: String) {
        webView.loadUrl(url)
    }

    override fun showRouteInfo(duration: String, distance: String) {
        btnGo.showView()
        containerRouteInfo.showView()
        tvRouteDuration.text = duration
        tvRouteDistance.text = distance
    }

    override fun showError(error: String) {
        toast(error)
    }

    override fun navigateToNavigator(routeJson: String) {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(
                        {
                            val intent = intentFor<NavigatorActivity>("data" to routeJson)
                            startActivityForResult(intent, 0)
                        }
                )

    }

    fun collapse(v: View, duration: Int, targetHeight: Int) {
        val prevHeight = v.height
        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            v.layoutParams.height = animation.animatedValue as Int
            v.requestLayout()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                v.hideView()
            }
        })
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }
}
