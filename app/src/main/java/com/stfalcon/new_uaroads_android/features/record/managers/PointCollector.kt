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
import android.hardware.SensorManager
import io.reactivex.Observable

/*
 * Created by Anton Bevza on 4/24/17.
 */
class PointCollector(context: Context, sensorManager: SensorManager) {

    private val locationManager = LocationManager(context)
    private val motionManager = MotionManager(sensorManager)

    fun getPointObservable(): Observable<Point> =
            Observable.merge<Point>(getCheckpointsObservable(), getPitsObservable())

    fun startCollectionData() {
        motionManager.startListenSensors()
    }

    fun stopCollectionData() {
        motionManager.stopListenSensors()
    }

    fun getFilteredLocationObservable() = locationManager.getFilteredLocationFlow()

    fun getPureLocationObservable() = locationManager.getPureLocationFlow()

    fun getForceValuesObservable() = motionManager.getForceValuesObservable()

    private fun getCheckpointsObservable() = locationManager.getFilteredLocationFlow()
            .map { Point.createCheckpoint(it.latitude, it.longitude, it.time, it.accuracy, it.speed) }

    private fun getPitsObservable() = motionManager.getPitsObservable()
            .map { Point.createPit(it.pit, it.time) }


}