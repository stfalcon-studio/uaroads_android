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

package com.stfalcon.new_uaroads_android.features.record.managers

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/24/17.
 */
internal class LocationManager @Inject constructor(val context: Context) {

    //Location
    private val MAX_DETECTING_SPEED: Long = 150 // km/h

    private val locationRequest: LocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(3000)

    internal fun getFilteredLocationFlow(): Observable<Location> =
            getPureLocationFlow()
                    .buffer(2, 1)//current and previous location
                    .filter {
                        val firstPointTime = it[0].time / 1000 //in sec
                        val nextPointTime = it[1].time / 1000 //in sec
                        val dTime = nextPointTime - firstPointTime // in sec
                        val dDistance = it[0].distanceTo(it[1]) // in m
                        val speed = (dDistance / dTime * 3.6).toFloat() //in km/h
                        it[1].speed = speed
                        pointIsValid(speed, dDistance, it[1].accuracy)
                    }.map { it[1] }

    internal fun getPureLocationFlow(): Observable<Location> {
        return RxLocation(context).location()
                .updates(locationRequest)
                .filter { it != null }
    }

    private fun pointIsValid(speed: Float, pointDistance: Float, accuracy: Float): Boolean {
        return speed < MAX_DETECTING_SPEED && pointDistance < 150 && pointDistance > accuracy / 2f
    }

}