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

import android.app.Service
import com.stfalcon.new_uaroads_android.features.autostart.AutoStartJobService
import com.stfalcon.new_uaroads_android.features.autostart.AutoStartServiceSubComponent
import com.stfalcon.new_uaroads_android.features.record.service.RecordService
import com.stfalcon.new_uaroads_android.features.record.service.RecordServiceSubComponent
import com.stfalcon.new_uaroads_android.features.tracks.autoupload.AutoUploadJobService
import com.stfalcon.new_uaroads_android.features.tracks.autoupload.AutoUploadServiceSubComponent
import com.stfalcon.new_uaroads_android.features.tracks.service.TracksServiceSubComponent
import com.stfalcon.new_uaroads_android.features.tracks.service.TracksUploadService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

/*
 * Created by Anton Bevza on 4/5/17.
 */

//TODO rename to "BuildersModule?" or add to package "builders"?
@Module
abstract class ServiceInjectionModule {

    @Binds
    @IntoMap
    @ServiceKey(RecordService::class)
    internal abstract fun bindRecordServiceInjectorFactory(
            builder: RecordServiceSubComponent.Builder): AndroidInjector.Factory<out Service>

    @Binds
    @IntoMap
    @ServiceKey(TracksUploadService::class)
    internal abstract fun bindTracksUploadServiceInjectorFactory(
            builder: TracksServiceSubComponent.Builder): AndroidInjector.Factory<out Service>

    @Binds
    @IntoMap
    @ServiceKey(AutoUploadJobService::class)
    internal abstract fun bindAutoUploadServiceInjectorFactory(
            builder: AutoUploadServiceSubComponent.Builder): AndroidInjector.Factory<out Service>

    @Binds
    @IntoMap
    @ServiceKey(AutoStartJobService::class)
    internal abstract fun bindAutoStartServiceInjectorFactory(
            builder: AutoStartServiceSubComponent.Builder): AndroidInjector.Factory<out Service>

}
