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

package com.stfalcon.new_uaroads_android.features.places

import android.location.Location
import com.stfalcon.mvphelper.IPresenter
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction

/*
 * Created by Anton Bevza on 4/11/17.
 */
class LocationChooserContract {
    interface View {
        fun navigateBack()
        fun showResults(list: List<GeoPlacePrediction>)
        fun showEmptyState(location: Location?)
        fun showError(error: String)
        fun showNoResutError(query: String)
        fun returnPlaceResult(placeTitle: String, placeLocation: LatLng)
        fun returnMyLocationResult()
    }

    interface Presenter: IPresenter<View> {
        fun onClickNavigateButton()
        fun onQueryChanged(query: String)
        fun onPlaceSelected(item: GeoPlacePrediction)
        fun onMyLocationSelected()
        fun onActionSearchClicked(query: String, text: String)
    }
}