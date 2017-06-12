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

package com.stfalcon.new_uaroads_android.common.injection.components

import com.stfalcon.new_uaroads_android.UaroadsApp
import com.stfalcon.new_uaroads_android.common.injection.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/*
 * Created by Anton Bevza on 3/28/17.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityInjectionModule::class,
        ServiceInjectionModule::class,
        NetworkModule::class,
        ServicesModule::class,
        LocationModule::class,
        PreferencesModule::class,
        AnalyticsModule::class)
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: UaroadsApp): Builder
        fun build(): AppComponent
    }

    fun inject(app: UaroadsApp)

}