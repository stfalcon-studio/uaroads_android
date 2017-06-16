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

package com.stfalcon.new_uaroads_android.common.database.models

import com.stfalcon.new_uaroads_android.features.record.managers.Point
import io.realm.RealmObject

/*
 * Created by Anton Bevza on 4/25/17.
 */

open class PointRealm : RealmObject() {
    open var pit: Double = 0.0
    open var time: Long = 0
    open var type: String? = null
    open var lat: Double = 0.0
    open var lng: Double = 0.0
    open var accuracy: Float = 0f
    open var speed: Float = 0f

    fun convertFromPoint(point: Point) {
        this.pit = point.pit
        this.time = point.time
        this.type = point.type
        this.lat = point.lat
        this.lng = point.lng
        this.accuracy = point.accuracy
        this.speed - point.accuracy
    }

    fun convertToPoint(): Point {
        if (type != null) {
            return when (type) {
                Point.POINT_TYPE_PIT -> Point.createPit(pit, time)
                Point.POINT_TYPE_CP -> Point.createCheckpoint(lat, lng, time, accuracy, speed)
                else -> throw IllegalArgumentException("Wrong point type")
            }
        } else {
            throw NullPointerException("Point type not assign")
        }
    }
}
