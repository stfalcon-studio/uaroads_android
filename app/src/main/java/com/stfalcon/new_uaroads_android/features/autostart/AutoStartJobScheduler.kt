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

import android.content.Context
import com.firebase.jobdispatcher.*
import com.stfalcon.new_uaroads_android.common.data.preferences.Settings
import javax.inject.Inject

/*
 * Created by Anton Bevza on 5/4/17.
 */
class AutoStartJobScheduler @Inject constructor(val context: Context, val settings: Settings) {
    val TAG_AUTO_START_JOB = "com.stfalcon.uaroads.AUTO_START_JOB"
    val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

    fun scheduleAutoStartService() {
        if (settings.isAutostartEnabled()) {
            dispatcher.cancel(TAG_AUTO_START_JOB)
            val job = dispatcher.newJobBuilder()
                    .setService(AutoStartJobService::class.java)
                    .setTag(TAG_AUTO_START_JOB)
                    .setLifetime(Lifetime.FOREVER)
                    .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                    .setTrigger(Trigger.executionWindow(0, 20))
                    .setRecurring(true)
                    .build()
            dispatcher.mustSchedule(job)
        }
    }

    fun cancelAll() {
        dispatcher.cancel(TAG_AUTO_START_JOB)
    }
}