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

import android.location.Location
import com.stfalcon.mvphelper.IPresenter
import com.stfalcon.new_uaroads_android.common.network.models.LatLng

/*
 * Created by Anton Bevza on 4/7/17.
 */

class FindRouteContract {
    interface View {
        fun showMyLocationButton()
        fun showFromAddress(address: String?)
        fun showToAddress(address: String?)
        fun showLocationChooser(key: Int, location: Location? = null)
        fun showMap(mapUrl: String)
        fun showBuildRouteButton()
        fun navigateToBestRoute(locationFrom: LatLng, locationTo: LatLng,
                                titleFrom: String, titleTo: String)
        fun showError(text: String)
        fun checkPermission()
    }

    interface Presenter : IPresenter<View> {
        fun onPermissionGained()
        fun onMyLocationClicked()
        fun onFromClicked()
        fun onToClicked()
        fun onFromSelectedPlace(placeTitle: String, placeLocation: LatLng)
        fun onToSelectedPlace(placeTitle: String, placeLocation: LatLng)
        fun onBuildRouteButtonClicked()
    }
}
