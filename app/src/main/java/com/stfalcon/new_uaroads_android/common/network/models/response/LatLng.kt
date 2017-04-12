package com.stfalcon.new_uaroads_android.common.network.models.response

import java.io.Serializable

/*
 * Created by Anton Bevza on 4/12/17.
 */
class LatLng : Serializable {

    var latitude: Double
    var longitude: Double

    constructor(lat: Double, lng: Double) {
        latitude = lat
        longitude = lng
    }

    constructor(pos: String) {
        val array = pos.split(" ")
        latitude = array[0].toDouble()
        longitude = array[1].toDouble()
    }
}