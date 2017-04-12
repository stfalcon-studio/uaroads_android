package com.stfalcon.new_uaroads_android.features.findroute

import android.location.Address
import android.location.Location
import com.patloew.rxlocation.RxLocation
import com.stfalcon.new_cookorama_android.common.ui.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/*
 * Created by Anton Bevza on 4/7/17.
 */

class FindRoutePresenter @Inject constructor(val view: FindRouteContract.View,
                                             val rxLocation: RxLocation) : BasePresenter(), FindRouteContract.Presenter {
    override fun onToClicked() {
        view.showLocationChooser(FindRouteFragment.KEY_GET_TO_LOCATION)
    }

    override fun onFromClicked() {
        disposables.add(getLastLocation()
                .toSingle()
                .subscribe({
                    view.showLocationChooser(FindRouteFragment.KEY_GET_TO_LOCATION, it)
                }, {
                    view.showLocationChooser(FindRouteFragment.KEY_GET_TO_LOCATION)
                }))
    }

    override fun onMyLocationClicked() {
        disposables.add(getLastLocation()
                .flatMapObservable { getAddressFromLocation(it) }
                .map { it.getAddressLine(0) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.showFromAddress(it)
                })
        )
    }

    private fun getLastLocation() = rxLocation.location().lastLocation()

    private fun getAddressFromLocation(location: Location): Observable<Address> {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io())
    }
}
