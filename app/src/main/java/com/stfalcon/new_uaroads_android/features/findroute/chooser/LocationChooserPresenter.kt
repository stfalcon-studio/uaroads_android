package com.stfalcon.new_uaroads_android.features.findroute.chooser

import android.location.Location
import com.stfalcon.new_cookorama_android.common.ui.BasePresenter
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng
import com.stfalcon.new_uaroads_android.repos.places.PlacesRepo
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/11/17.
 */
class LocationChooserPresenter @Inject constructor(val view: LocationChooserContract.View,
                                                   val location: Location?,
                                                   val placesRepo: PlacesRepo)
    : BasePresenter(), LocationChooserContract.Presenter {
    private val list = mutableListOf<GeoPlacePrediction>()

    override fun onClickNavigateButton() {
        view.navigateBack()
    }

    override fun onQueryChanged(query: String) {
        val latLng = if (location != null) LatLng(location.latitude, location.longitude) else null
        list.clear()
        placesRepo.getPlacesPredication(query, latLng)
                .subscribe(
                        {
                            if (it.isNotEmpty()) {
                                view.showResults(it)
                            } else {
                                location?.apply { view.showEmptyState(this) }
                            }
                        },
                        {
                            view.showError(it.localizedMessage)
                        })
    }


}