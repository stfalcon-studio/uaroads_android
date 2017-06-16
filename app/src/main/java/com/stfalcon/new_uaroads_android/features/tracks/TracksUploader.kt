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

package com.stfalcon.new_uaroads_android.features.tracks

import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.data.preferences.Settings
import com.stfalcon.new_uaroads_android.common.database.RealmService
import com.stfalcon.new_uaroads_android.common.database.models.TrackRealm
import com.stfalcon.new_uaroads_android.common.injection.InjectionConsts
import com.stfalcon.new_uaroads_android.ext.log
import com.stfalcon.new_uaroads_android.repos.tracks.TracksRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by Anton Bevza on 5/4/17.
 */
class TracksUploader @Inject constructor(val realmService: RealmService,
                                         val tracksRepo: TracksRepo,
                                         val settings: Settings,
                                         val analyticsManager: AnalyticsManager,
                                         @Named(InjectionConsts.NAME_USER_ID) val userId: String,
                                         @Named(InjectionConsts.NAME_SENSOR_INFO) val sensorInfo: String) {


    fun sendTrack(trackId: String): Completable {
        val track = realmService.getTrack(trackId)
        realmService.updateTrackStatus(trackId, TrackRealm.STATUS_IS_SENDING)
        return tracksRepo.sendTrack(userId, track.points.map { it.convertToPoint() },
                trackId, track.comment ?: "", track.comment ?: "", track.autoRecord)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    realmService.updateTrackStatus(trackId, TrackRealm.STATUS_SENT)
                    analyticsManager.sendTrackAutoSent()
                }
                .onErrorResumeNext { t ->
                    if (t is HttpException && t.code() == 401) {
                        tracksRepo.sendDeviceInfo(settings.getUserEmail() ?: "", sensorInfo, userId)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnComplete { sendTrack(trackId) }
                    } else throw t
                }
                .doOnComplete { log("Track $trackId is sent") }
                .doOnError {
                    realmService.updateTrackStatus(trackId, TrackRealm.STATUS_NOT_SENT)
                    it.message?.let { log(it) }
                }
    }

    fun sendAllTracks(): Completable {
        return Observable.fromIterable(realmService.getAllUnsentTracks())
                .map { sendTrack(it.id) }
                .toList()
                .flatMapCompletable { Completable.mergeDelayError(it) }
    }


}