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

package com.stfalcon.new_uaroads_android.features.findroute

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import com.stfalcon.mvphelper.MvpFragment
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.RevealAnimationEvent
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.ext.log
import com.stfalcon.new_uaroads_android.ext.showView
import com.stfalcon.new_uaroads_android.features.bestroute.BestRouteActivity
import com.stfalcon.new_uaroads_android.features.places.LocationChooserActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_find_route.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


/*
 * Created by Anton Bevza on 4/7/17.
 */
class FindRouteFragment : MvpFragment<FindRouteContract.Presenter, FindRouteContract.View>(), FindRouteContract.View {

    override fun getLayoutResId() = R.layout.fragment_find_route

    lateinit var rxPermissions: RxPermissions

    companion object {
        val KEY_GET_TO_LOCATION = 1
        val KEY_GET_FROM_LOCATION = 2

        fun newInstance(): Fragment {
            return FindRouteFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rxPermissions = RxPermissions(activity)

        initFromToViews()
        initWebView()
    }

    private fun initFromToViews() {
        btnMyLocation.setOnClickListener { presenter?.onMyLocationClicked() }
        tvFrom.setOnClickListener { presenter?.onFromClicked() }
        tvTo.setOnClickListener { presenter?.onToClicked() }
        btnBuildRoute.setOnClickListener { presenter?.onBuildRouteButtonClicked() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.setWebViewClient(WebViewClient())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            LocationChooserActivity.RESULT_PLACE -> {
                val placeTitle = data?.getStringExtra(LocationChooserActivity.KEY_PLACE_TITLE) as String
                val placeLocation = data.getSerializableExtra(LocationChooserActivity.KEY_PLACE_LOCATION) as LatLng
                when (requestCode) {
                    KEY_GET_FROM_LOCATION -> presenter?.onFromSelectedPlace(placeTitle, placeLocation)
                    KEY_GET_TO_LOCATION -> presenter?.onToSelectedPlace(placeTitle, placeLocation)
                }
            }
            LocationChooserActivity.RESULT_MY_LOCATION -> {
                presenter?.onMyLocationClicked()
            }
        }
    }

    override fun showMyLocationButton() {
        btnMyLocation.showView()
    }

    override fun showFromAddress(address: String?) {
        tvFrom.text = address
    }

    override fun showToAddress(address: String?) {
        tvTo.text = address
    }

    override fun showLocationChooser(key: Int, location: Location?) {
        startActivityForResult(LocationChooserActivity.getIntent(activity, location), key)
    }

    override fun showMap(mapUrl: String) {
        webView.loadUrl(mapUrl)
    }

    override fun showBuildRouteButton() {
        btnBuildRoute.show()
        btnBuildRoute.bringToFront()
    }

    override fun navigateToBestRoute(locationFrom: LatLng, locationTo: LatLng, titleFrom: String, titleTo: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val x = (btnBuildRoute.x + btnBuildRoute.width / 2).toInt()
            val y = (btnBuildRoute.y + btnBuildRoute.height / 2).toInt()

            val event = RevealAnimationEvent(x, y, R.color.colorAccent, {
                startActivity(BestRouteActivity.getIntent(activity, locationFrom, locationTo, titleFrom, titleTo))
                activity.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave)
            })

            EventBus.getDefault().post(event)
        } else {
            startActivity(BestRouteActivity.getIntent(activity, locationFrom, locationTo, titleFrom, titleTo))
        }
    }

    override fun showError(text: String) {
        activity.toast(text)
    }

    override fun checkPermission() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(
                        {
                            if (!it) {
                                activity.toast(R.string.error_location_permission_not_granted)
                                activity.finish()
                            } else {
                                presenter?.onPermissionGained()
                            }
                        },
                        {
                            it.localizedMessage?.let { log(it) }
                        }
                )
    }
}