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

import android.content.Context
import android.hardware.SensorManager
import android.telephony.TelephonyManager
import com.stfalcon.new_uaroads_android.features.record.managers.PointCollector
import dagger.Module
import dagger.Provides

/*
 * Created by Anton Bevza on 4/7/17.
 */
@Module
class RecordServiceModule {

    @Provides
    fun provideSensorManager(service: RecordService): SensorManager =
            service.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    fun provideTelephonyManager(service: RecordService): TelephonyManager =
            service.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @Provides
    fun providePointCollector(service: RecordService, sensorManager: SensorManager) =
            PointCollector(service, sensorManager)

}