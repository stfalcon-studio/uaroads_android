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
import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.repos.places.PlacesRepo
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/11/17.
 */
class LocationChooserPresenter @Inject constructor(val location: Location?,
                                                   val placesRepo: PlacesRepo,
                                                   val analyticsManager: AnalyticsManager)
    : Presenter<LocationChooserContract.View>(), LocationChooserContract.Presenter {

    override fun onClickNavigateButton() {
        view?.navigateBack()
    }

    override fun onQueryChanged(query: String) {
        val latLng = if (location != null) LatLng(location.latitude, location.longitude) else null
        disposables.add(placesRepo.getPlacesPredication(query, latLng)
                .subscribe(
                        {
                            if (it.isNotEmpty()) {
                                view?.showResults(it)
                            } else {
                                view?.showEmptyState(location)
                            }
                        },
                        {
                            it?.message?.let { view?.showError(it) }
                        })
        )
    }

    override fun onPlaceSelected(item: GeoPlacePrediction) {
        view?.returnPlaceResult(item.place.name, item.place.point.position)
    }

    override fun onMyLocationSelected() {
        view?.returnMyLocationResult()
    }

    override fun onViewAttached(view: LocationChooserContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        view.showEmptyState(location)
        analyticsManager.sendScreen("SearchPlaceActivity")
    }

    override fun onActionSearchClicked(query: String, text: String) {
        val latLng = if (location != null) LatLng(location.latitude, location.longitude) else null
        disposables.add(placesRepo.getPlacesPredication(query, latLng)
                .subscribe(
                        {
                            if (it.isNotEmpty()) {
                                onPlaceSelected(it[0])
                            } else {
                                view?.showNoResutError(text)
                            }
                        },
                        {
                            it?.message?.let { view?.showError(it) }
                        })
        )
    }
}