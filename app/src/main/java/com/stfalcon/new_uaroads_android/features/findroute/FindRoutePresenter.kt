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

import android.location.Address
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.ext.safeLet
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/*
 * Created by Anton Bevza on 4/7/17.
 */

class FindRoutePresenter @Inject constructor(val rxLocation: RxLocation,
                                             val analyticsManager: AnalyticsManager)
    : Presenter<FindRouteContract.View>(), FindRouteContract.Presenter {

    val MAIN_MAP_URL = "http://uaroads.com/static-map?mob=true&lat=%lat%&lon=%lon%&zoom=%zoom%"
    val UKRAINE_DEF_LAT = 49.3864569
    val UKRAINE_DEF_LNG = 31.6182803

    private var locationFrom: LatLng? = null
    private var titleFrom: String? = null
    private var locationTo: LatLng? = null
    private var titleTo: String? = null
    private var lastPosition: Location? = null
    val locationRequest: LocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(3000)

    override fun onViewAttached(view: FindRouteContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        view.checkPermission()
        view.showFromAddress(titleFrom)
        view.showToAddress(titleTo)
        tryToShowBuildRouteButton()
    }

    override fun onPermissionGained() {
        disposables.add(
                rxLocation.settings().checkAndHandleResolution(locationRequest)
                        .flatMapMaybe { getLastLocation() }
                        .toSingle()
                        .doOnError {
                            view?.showMap(getMapUrl(UKRAINE_DEF_LAT, UKRAINE_DEF_LNG, 5))
                        }
                        .retryWhen { getNewLocation().toFlowable(BackpressureStrategy.BUFFER) }
                        .subscribe(
                                { location ->
                                    lastPosition = location
                                    view?.showMap(getMapUrl(location.latitude, location.longitude, 10))
                                    view?.showMyLocationButton()
                                },
                                {
                                    it?.message?.let { view?.showError(it) }
                                }
                        ))
    }

    override fun onToClicked() {
        view?.showLocationChooser(FindRouteFragment.KEY_GET_TO_LOCATION)
    }

    override fun onFromClicked() {
        view?.showLocationChooser(FindRouteFragment.KEY_GET_FROM_LOCATION, lastPosition)
    }

    override fun onMyLocationClicked() {
        lastPosition?.let { lastLocation ->
            locationFrom = LatLng(lastLocation.latitude, lastLocation.longitude)
            disposables.add(
                    getAddressFromLocation(lastLocation)
                            .map { it.getAddressLine(0) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        view?.showFromAddress(it)
                                        titleFrom = it
                                    },
                                    {
                                        it?.message?.let { view?.showError(it) }
                                    }
                            ))
        }
    }

    override fun onFromSelectedPlace(placeTitle: String, placeLocation: LatLng) {
        locationFrom = placeLocation
        titleFrom = placeTitle
        view?.showFromAddress(placeTitle)
        tryToShowBuildRouteButton()
    }

    override fun onToSelectedPlace(placeTitle: String, placeLocation: LatLng) {
        locationTo = placeLocation
        titleTo = placeTitle
        view?.showToAddress(placeTitle)
        tryToShowBuildRouteButton()
    }

    override fun onBuildRouteButtonClicked() {
        safeLet(locationFrom, locationTo, titleFrom, titleTo) { it1, it2, it3, it4 ->
            view?.navigateToBestRoute(it1, it2, it3, it4)

            analyticsManager.sendSearchAction(it3, it4)
        }
    }

    private fun tryToShowBuildRouteButton(){
        if (canBuildRoute()) {
            view?.showBuildRouteButton()
        }
    }

    private fun getNewLocation() = rxLocation.location().updates(locationRequest)

    private fun getLastLocation() = rxLocation.location().lastLocation()

    private fun canBuildRoute() = locationFrom != null && locationTo != null

    private fun getAddressFromLocation(location: Location): Observable<Address> {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io())
    }

    private fun getMapUrl(lat: Double, lng: Double, zoom: Int) =
            MAIN_MAP_URL.replace("%lat%", lat.toString())
                    .replace("%lon%", lng.toString())
                    .replace("%zoom%", zoom.toString())
}
