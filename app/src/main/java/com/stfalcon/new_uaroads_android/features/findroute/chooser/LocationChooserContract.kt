package com.stfalcon.new_uaroads_android.features.findroute.chooser

import android.location.Location
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction

/*
 * Created by Anton Bevza on 4/11/17.
 */
class LocationChooserContract {
    interface View {
        fun navigateBack()
        fun showResults(list: List<GeoPlacePrediction>)
        fun showEmptyState(location: Location)
        fun showError(error: String)
    }

    interface Presenter {
        fun onClickNavigateButton()
        fun onQueryChanged(query: String)
    }
}