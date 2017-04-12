package com.stfalcon.new_uaroads_android.repos.places

import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng
import io.reactivex.Single

/*
 * Created by Anton Bevza on 4/12/17.
 */
interface PlacesRepo {
    fun getPlacesPredication(query: String, location: LatLng?): Single<MutableList<GeoPlacePrediction>>
}