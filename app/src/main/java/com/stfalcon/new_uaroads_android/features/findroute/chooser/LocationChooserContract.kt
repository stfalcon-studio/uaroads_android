package com.stfalcon.new_uaroads_android.features.findroute.chooser

import android.location.Location
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng

/*
 * Created by Anton Bevza on 4/11/17.
 */
class LocationChooserContract {
    interface View {
        fun navigateBack()
        fun showResults(list: List<GeoPlacePrediction>)
        fun showEmptyState(location: Location?)
        fun showError(error: String)
        fun returnPlaceResult(placeTitle: String, placeLocation: LatLng)
        fun returnMyLocationResult()
    }

    interface Presenter {
        fun onClickNavigateButton()
        fun onQueryChanged(query: String)
        fun onPlaceSelected(item: GeoPlacePrediction)
        fun onMyLocationSelected()
        fun onViewCreated()
    }
}