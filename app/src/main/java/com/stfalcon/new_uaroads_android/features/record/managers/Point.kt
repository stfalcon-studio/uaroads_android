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

/*
 * Created by Anton Bevza on 4/24/17.
 */
class Point {

    companion object {
        const val POINT_TYPE_CP = "cp"
        const val POINT_TYPE_PIT = "origin"

        fun createCheckpoint(lat: Double, lng: Double, time: Long, accuracy: Float, speed: Float)
                = Point(lat, lng, time, accuracy, speed)

        fun createPit(pit: Double, time: Long) =
                Point(pit, time)

    }

    val pit: Double
    val time: Long
    val type: String
    val lat: Double
    val lng: Double
    val accuracy: Float
    val speed: Float

    private constructor(lat: Double, lng: Double, time: Long, accuracy: Float, speed: Float) {
        this.lat = lat
        this.lng = lng
        this.accuracy = accuracy
        this.speed = speed
        this.pit = 0.0
        this.time = time
        this.type = POINT_TYPE_CP
    }

    private constructor(pit: Double, time: Long) {
        this.pit = pit
        this.time = time
        this.lat = 0.0
        this.lng = 0.0
        this.accuracy = 0f
        this.speed = 0f
        this.type = POINT_TYPE_PIT
    }


}