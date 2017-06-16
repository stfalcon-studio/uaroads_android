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

package com.stfalcon.new_uaroads_android.common.database

import com.stfalcon.new_uaroads_android.common.database.models.PointRealm
import com.stfalcon.new_uaroads_android.common.database.models.TrackRealm
import com.stfalcon.new_uaroads_android.features.record.managers.Point
import io.realm.Realm
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by Anton Bevza on 4/25/17.
 */
class RealmService(val realm: Realm) {

    fun createTrack(id: String, isAutoRecord: Boolean) {
        realm.executeTransactionAsync { realm ->
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val track = realm.createObject(TrackRealm::class.java, id)
            track.status = TrackRealm.STATUS_IS_RECODING
            track.comment = dateFormat.format(Date())
            track.timestamp = System.currentTimeMillis()
            track.autoRecord = if (isAutoRecord) 1 else 0
        }
    }

    fun getTrack(id: String): TrackRealm {
        return realm.where(TrackRealm::class.java)
                .equalTo("id", id).findFirst()
    }

    fun getAllTracks(): RealmResults<TrackRealm> {
        return realm.where(TrackRealm::class.java)
                .findAllSorted("timestamp")
    }

    fun getAllTracksDistance(): Int {
        return realm.where(TrackRealm::class.java)
                .findAll()
                .map { it.distance }
                .sum()
    }

    fun getAllRecordedTracks(): RealmResults<TrackRealm> {
        return realm.where(TrackRealm::class.java)
                .notEqualTo("status", TrackRealm.STATUS_IS_RECODING)
                .findAllSorted("timestamp")
    }

    fun getAllUnsentTracks(): RealmResults<TrackRealm> {
        return realm.where(TrackRealm::class.java)
                .equalTo("status", TrackRealm.STATUS_NOT_SENT)
                .findAll()
    }

    fun getLastTrackDistance(): Int {
        return realm.where(TrackRealm::class.java)
                .notEqualTo("status", TrackRealm.STATUS_IS_RECODING)
                .findAllSorted("timestamp")
                .last(null)?.distance ?: 0
    }

    fun deleteTrack(id: String) {
        realm.executeTransaction {
            it.where(TrackRealm::class.java).
                    equalTo("id", id).findFirst()?.deleteFromRealm()
        }
    }

    fun updateTrackStatus(id: String, status: Int) {
        realm.executeTransactionAsync {
            it.where(TrackRealm::class.java)
                    .equalTo("id", id)
                    .findFirst()
                    .status = status
        }
    }

    fun updateTrackDistance(id: String, distance: Int) {
        realm.executeTransactionAsync {
            it.where(TrackRealm::class.java)
                    .equalTo("id", id)
                    .findFirst()
                    .distance = distance
        }
    }

    fun savePoints(trackId: String, points: MutableList<Point>) {
        realm.executeTransactionAsync { realm ->
            val track = realm.where(TrackRealm::class.java).equalTo("id", trackId).findFirst()
            if (track != null) {
                points.forEach {
                    val realmPoint = realm.createObject(PointRealm::class.java)
                    realmPoint.convertFromPoint(it)
                    track.points.add(realmPoint)
                }
            }
        }
    }

    fun closeRealm() {
        realm.close()
    }

}