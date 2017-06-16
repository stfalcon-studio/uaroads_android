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

package com.stfalcon.new_uaroads_android.common.injection.modules

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Anton Bevza on 5/13/17.
 */
@Module
class AnalyticsModule {

    @Singleton
    @Provides
    fun provideTracker(context: Context): Tracker {
        val googleAnalytics = GoogleAnalytics.getInstance(context)
        return googleAnalytics.newTracker("UA-44978148-13")
    }

    @Singleton
    @Provides
    fun provideAnalyticasManager(tracker: Tracker): AnalyticsManager = AnalyticsManagerImpl(tracker)

}