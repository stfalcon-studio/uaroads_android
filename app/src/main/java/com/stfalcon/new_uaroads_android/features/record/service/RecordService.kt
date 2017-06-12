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

package com.stfalcon.new_uaroads_android.features.record.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.database.RealmService
import com.stfalcon.new_uaroads_android.common.database.models.TrackRealm
import com.stfalcon.new_uaroads_android.ext.log
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import com.stfalcon.new_uaroads_android.features.record.managers.Point
import com.stfalcon.new_uaroads_android.features.record.managers.PointCollector
import com.stfalcon.new_uaroads_android.features.tracks.autoupload.AutoUploaderJobScheduler
import dagger.android.DaggerService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/20/17.
 */
class RecordService : DaggerService(), IRecordService {

    //region Static
    companion object {
        val STATE_IDLE = 1
        val STATE_RECORD = 2
        val STATE_PAUSE = 3
        val KEY_ACTION = "action"
        val ACTION_START_INDEPENDENTLY = "start_independently"
        val ACTION_STOP_INDEPENDENTLY = "stop_independently"
        val ACTION_START_WITH_NAVIGATOR = "start_with_navigator"
        val ACTION_STOP_WITH_NAVIGATOR = "stop_with_navigator"

        fun startIndependently(context: Context) {
            context.startService(context.intentFor<RecordService>().putExtra(KEY_ACTION, ACTION_START_INDEPENDENTLY))
        }

        fun stopIndependently(context: Context) {
            context.startService(context.intentFor<RecordService>().putExtra(KEY_ACTION, ACTION_STOP_INDEPENDENTLY))
        }
    }
    //endregion

    //region Injection fields
    @Inject lateinit var pointCollector: PointCollector
    @Inject lateinit var realmService: RealmService
    @Inject lateinit var autoUploaderJobScheduler: AutoUploaderJobScheduler
    @Inject lateinit var analyticsManager: AnalyticsManager
    @Inject lateinit var telManager: TelephonyManager
    //endregion

    //region Public fields
    val mBinder = LocalBinder()
    //endregion

    //region Private fields
    private val NOTIFICATION = 2525
    private lateinit var stateObserver: BehaviorSubject<Int>
    private lateinit var distanceObserver: BehaviorSubject<Int>
    private var currentTrackId: String? = null
    private val disposables = CompositeDisposable()
    private var isRunningIndependently = false
    //endregion

    //region Lifecycle methods
    override fun onBind(intent: Intent?): IBinder? = mBinder

    override fun onCreate() {
        super.onCreate()

        stateObserver = BehaviorSubject.create()
        distanceObserver = BehaviorSubject.create()

        telManager.listen(RecordPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("service started")

        intent?.extras?.let {
            if (it.containsKey(KEY_ACTION)) {
                when (it.get(KEY_ACTION)) {
                    ACTION_START_INDEPENDENTLY -> {
                        if (currentTrackId == null) {
                            isRunningIndependently = true
                            startRecord()
                            analyticsManager.sendStartAutoRecord()
                        }
                    }
                    ACTION_STOP_INDEPENDENTLY -> {
                        if (isRunningIndependently && currentTrackId != null) {
                            stopRecord()
                            analyticsManager.sendStopAutoRecord()
                        }
                    }
                    ACTION_START_WITH_NAVIGATOR -> {
                        if (currentTrackId == null) {
                            startRecord()
                        }
                    }
                    ACTION_STOP_WITH_NAVIGATOR -> {
                        if (currentTrackId != null) {
                            stopRecord()
                        }
                    }
                }
            }

        }

        return START_STICKY
    }

    override fun onDestroy() {
        log("service destroyed")
        stopRecord()
        realmService.closeRealm()
        super.onDestroy()
    }
    //endregion

    //region Public methods
    override fun startRecord() {
        stateObserver.onNext(STATE_RECORD)
        showRecordNotification()
        currentTrackId = UUID.randomUUID().toString()
        currentTrackId?.let { realmService.createTrack(it, isRunningIndependently) }
        disposables.add(pointCollector.getPointObservable()
                .doOnNext {
                    if (it.type == Point.POINT_TYPE_CP) {
                        log("cp")
                    } else {
                        log("origin")
                    }
                }
                .buffer(20)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ points ->
                    currentTrackId?.let { realmService.savePoints(it, points) }
                },
                        {
                            if (it is SecurityException) {
                                toast(R.string.error_location_permission_not_granted)
                            }
                        })
        )
        disposables.add(pointCollector.getFilteredLocationObservable()
                .buffer(2, 1)
                .subscribe({
                    val distance = it[0].distanceTo(it[1]).toInt() // in m
                    currentTrackId?.let { trackId ->
                        realmService.getTrack(trackId).let {
                            val trackDistance = it.distance + distance
                            realmService.updateTrackDistance(trackId, trackDistance)
                            distanceObserver.onNext(trackDistance)
                        }
                    }
                }, {
                    if (it is SecurityException) {
                        toast(R.string.error_location_permission_not_granted)
                    }
                })
        )
        distanceObserver.onNext(0)
        pointCollector.startCollectionData()
    }

    override fun stopRecord() {
        stopForeground(true)
        pointCollector.stopCollectionData()
        disposables.clear()

        currentTrackId?.let {
            val track = realmService.getTrack(it)
            if (track.distance > 100) {
                realmService.updateTrackStatus(it, TrackRealm.STATUS_NOT_SENT)
                autoUploaderJobScheduler.scheduleAutoUploadingService()
            } else {
                realmService.deleteTrack(it)
            }
        }
        currentTrackId = null
        stateObserver.onNext(STATE_IDLE)
        isRunningIndependently = false
        stopSelf()
    }

    override fun pauseRecord() {
        stateObserver.onNext(STATE_PAUSE)
        showRecordNotification(pause = true)
        pointCollector.stopCollectionData()
    }

    override fun continueRecord() {
        stateObserver.onNext(STATE_RECORD)
        showRecordNotification()
        pointCollector.startCollectionData()
    }

    override fun getStateFlow(): Observable<Int> {
        return stateObserver
    }

    override fun getForceValuesFlow(): Observable<Double> {
        return pointCollector.getForceValuesObservable()
    }

    override fun getTrackDistanceFlow(): Observable<Int> {
        return distanceObserver
    }

    override fun getPureLocationFlow(): Observable<Location> {
        return pointCollector.getPureLocationObservable()
    }
    //endregion

    //region Private methods
    private fun showRecordNotification(pause: Boolean = false) {
        val text = if (pause) getString(R.string.ntf_pause) else getString(R.string.ntf_recording)
        val contentIntent = PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), 0)

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(if (pause) R.drawable.ic_notify_pause else R.drawable.ic_notify_play)
                .setContentTitle(text)
                .setContentIntent(contentIntent)
                .setContentText(getString(R.string.app_name)) as NotificationCompat.Builder

        startForeground(NOTIFICATION, mBuilder.build())
    }
    //endregion

    //region LocalBinder
    inner class LocalBinder : Binder() {
        internal val service: IRecordService
            get() = this@RecordService
    }
    //endregion

    //region RecordPhoneStateListener
    inner class RecordPhoneStateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    if (currentTrackId != null) {
                        Handler().postDelayed({ continueRecord() }, 3000)
                    }
                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    if (currentTrackId != null) {
                        pauseRecord()
                    }
                }
            }
        }
    }
    //endregion
}