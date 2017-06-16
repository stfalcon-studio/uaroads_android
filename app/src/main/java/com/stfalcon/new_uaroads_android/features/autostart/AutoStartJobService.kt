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

package com.stfalcon.new_uaroads_android.features.autostart

import android.os.Bundle
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.DetectedActivity
import com.stfalcon.new_uaroads_android.ext.log
import com.stfalcon.new_uaroads_android.features.record.service.RecordService
import dagger.android.AndroidInjection


/*
 * Created by Anton Bevza on 5/5/17.
 */
class AutoStartJobService : JobService(), GoogleApiClient.ConnectionCallbacks {

    lateinit var client: GoogleApiClient
    lateinit var job: JobParameters

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(job: JobParameters): Boolean {
        log("started job")
        this.job = job
        client = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Awareness.API)
                .build()

        client.connect()

        return true
    }

    override fun onConnected(p0: Bundle?) {
        Awareness.SnapshotApi.getDetectedActivity(client)
                .setResultCallback {
                    if (!it.status.isSuccess) {
                        log("Could not get the current activity.")
                    } else {
                        val ar = it.activityRecognitionResult
                        val probableActivity = ar.mostProbableActivity

                        if (probableActivity.type == DetectedActivity.IN_VEHICLE) {
                            RecordService.startIndependently(this)
                        } else {
                            RecordService.stopIndependently(this)
                        }
                        log(probableActivity.toString())
                    }
                    jobFinished(job, false)
                }
    }

    override fun onConnectionSuspended(p0: Int) {
        log("job connection suspended")
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        log("stopped job")
        return false
    }

    override fun onDestroy() {
        log("destroyed")
        super.onDestroy()
    }

}