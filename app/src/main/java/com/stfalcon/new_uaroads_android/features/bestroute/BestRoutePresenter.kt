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

import com.google.gson.Gson
import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.injection.InjectionConsts
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.Route
import com.stfalcon.new_uaroads_android.repos.routes.RoutesRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by Anton Bevza on 4/14/17.
 */
class BestRoutePresenter @Inject constructor(val repo: RoutesRepo,
                                             val gson: Gson,
                                             val routeInfoFormatter: RouteInfoFormatter,
                                             @Named(InjectionConsts.NAME_LOCATION_FROM) val locationFrom: LatLng,
                                             @Named(InjectionConsts.NAME_LOCATION_TO) val locationTo: LatLng,
                                             @Named(InjectionConsts.NAME_TITLE_FROM) val titleFrom: String,
                                             @Named(InjectionConsts.NAME_TITLE_TO) val titleTo: String,
                                             val analyticsManager: AnalyticsManager)
    : Presenter<BestRouteContract.View>(), BestRouteContract.Presenter {

    val BEST_ROUTE_URL = "http://uaroads.com/routing/%latFrom%,%lonFrom%/%latTo%,%lonTo%?mob=true"
    var routeInstructions: String? = null
    var routeInfo: Route? = null
    var isLoading = false

    override fun onViewAttached(view: BestRouteContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        if (created) {
            view.setFromToInfo(titleFrom, titleTo)
            view.showMap(getBestRouteUrl(locationFrom, locationTo))
            loadInstructions()
        }
        if (!isLoading) {
            routeInfo?.routeSummary?.let {
                view.showRouteInfo(
                        routeInfoFormatter.formatDuration(it.totalTime),
                        routeInfoFormatter.formatDistance(it.totalDistance)
                )
            }
        }

        analyticsManager.sendScreen("BestRoadActivity")
    }


    override fun onClickNavigateButton() {
        view?.navigateBack()
    }

    override fun onClickGoButton() {
        routeInstructions?.let {
            view?.navigateToNavigator(it)
            analyticsManager.sendNavigationAction()
        }
    }

    private fun loadInstructions() {
        isLoading = true
        repo.getRouteInstruction(locationFrom, locationTo)
                .doOnSuccess { routeInstructions = it }
                .observeOn(AndroidSchedulers.mainThread())
                .map { gson.fromJson(it, Route::class.java) }
                .doAfterTerminate { isLoading = false }
                .subscribe({
                    routeInfo = it
                    if (it.routeSummary != null) {
                        view?.showRouteInfo(
                                routeInfoFormatter.formatDuration(it.routeSummary.totalTime),
                                routeInfoFormatter.formatDistance(it.routeSummary.totalDistance)
                        )
                    } else {
                        view?.showError(it.statusMessage)
                    }
                }, {
                    it.message?.let { view?.showError(it) }
                })
    }

    private fun getBestRouteUrl(locationFrom: LatLng, locationTo: LatLng) =
            BEST_ROUTE_URL.replace("%latFrom%", locationFrom.latitude.toString())
                    .replace("%lonFrom%", locationFrom.longitude.toString())
                    .replace("%latTo%", locationTo.latitude.toString())
                    .replace("%lonTo%", locationTo.longitude.toString())
}