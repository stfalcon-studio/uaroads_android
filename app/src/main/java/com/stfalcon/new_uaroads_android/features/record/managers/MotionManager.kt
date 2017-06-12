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

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/21/17.
 */
internal class MotionManager @Inject constructor(val sensorManager: SensorManager)
    : SensorEventListener {

    val SENSOR_DELAY_36Hz = 27777 // in  microseconds
    private var gX = 0f
    private var gY = 0f
    private var gZ = 0f
    private var sensorObservable = PublishSubject.create<SensorEvent>()

    fun startListenSensors() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SENSOR_DELAY_36Hz)
    }

    fun stopListenSensors() {
        sensorManager.unregisterListener(this)
    }

    fun getPitsObservable(): Observable<Pit> {
        return sensorObservable
                .subscribeOn(Schedulers.computation())
                .map {
                    Pit(toForceValue(it), System.currentTimeMillis())
                }
                .buffer(500, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .map { it.maxBy { it.pit } }
    }

    fun getForceValuesObservable(): Observable<Double> {
        return sensorObservable
                .subscribeOn(Schedulers.computation())
                .map { toForceValue(it) }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        sensorObservable.onNext(event)
    }

    private fun toForceValue(it: SensorEvent): Double {
        gX = it.values[0] / SensorManager.GRAVITY_EARTH
        gY = it.values[1] / SensorManager.GRAVITY_EARTH
        gZ = it.values[2] / SensorManager.GRAVITY_EARTH
        return Math.abs(Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()) - 1)
    }

    data class Pit(val pit: Double, val time: Long)
}