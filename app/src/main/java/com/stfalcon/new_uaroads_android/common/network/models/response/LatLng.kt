package com.stfalcon.new_uaroads_android.common.network.models.response

/*
 * Created by Anton Bevza on 4/12/17.
 */
class LatLng {

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