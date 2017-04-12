package com.stfalcon.new_uaroads_android.features.findroute

import android.location.Location
import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng

/*
 * Created by Anton Bevza on 4/7/17.
 */

class FindRouteContract {
    interface View {
        fun showFromAddress(address: String)
        fun showToAddress(address: String)
        fun showLocationChooser(key : Int, location: Location? = null)
    }

    interface Presenter {
        fun onMyLocationClicked()
        fun onFromClicked()
        fun onToClicked()
        fun onFromSelectedPlace(placeTitle: String, placeLocation: LatLng)
        fun onToSelectedPlace(placeTitle: String, placeLocation: LatLng)
    }
}
