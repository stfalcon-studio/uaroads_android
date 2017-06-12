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

import android.app.Activity
import com.stfalcon.new_uaroads_android.features.bestroute.BestRouteActivity
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import com.stfalcon.new_uaroads_android.features.main.MainScreenSubComponent
import com.stfalcon.new_uaroads_android.features.places.BestRouteSubComponent
import com.stfalcon.new_uaroads_android.features.places.LocationChooserActivity
import com.stfalcon.new_uaroads_android.features.places.LocationChooserSubComponent
import com.stfalcon.new_uaroads_android.features.settings.SettingsActivity
import com.stfalcon.new_uaroads_android.features.settings.SettingsScreenSubComponent
import com.stfalcon.new_uaroads_android.features.settings.TracksSubComponent
import com.stfalcon.new_uaroads_android.features.tracks.TracksActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

/*
 * Created by Anton Bevza on 4/5/17.
 */

//TODO rename to "BuildersModule?" or add to package "builders"?
@Module
abstract class ActivityInjectionModule {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivityInjectorFactory(
            builder: MainScreenSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(LocationChooserActivity::class)
    internal abstract fun bindLocationChooserActivityInjectorFactory(
            builder: LocationChooserSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(BestRouteActivity::class)
    internal abstract fun bindBestRouteInjectorFactory(
            builder: BestRouteSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(SettingsActivity::class)
    internal abstract fun bindSettingsActivityInjectorFactory(
            builder: SettingsScreenSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(TracksActivity::class)
    internal abstract fun bindTracksActivityInjectorFactory(
            builder: TracksSubComponent.Builder): AndroidInjector.Factory<out Activity>
}
