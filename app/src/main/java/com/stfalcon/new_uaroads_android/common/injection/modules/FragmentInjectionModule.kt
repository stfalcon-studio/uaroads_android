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

/*
 * Created by Anton Bevza on 4/5/17.
 */

import android.support.v4.app.Fragment
import com.stfalcon.new_uaroads_android.features.findroute.FindRouteFragment
import com.stfalcon.new_uaroads_android.features.findroute.FindRouteSubComponent
import com.stfalcon.new_uaroads_android.features.record.RecordSubComponent
import com.stfalcon.new_uaroads_android.features.record.RecordFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(FindRouteSubComponent::class, RecordSubComponent::class))
abstract class FragmentInjectionModule {
    @Binds
    @IntoMap
    @FragmentKey(FindRouteFragment::class)
    internal abstract fun bindFindRouteFragmentInjectorFactory(builder: FindRouteSubComponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(RecordFragment::class)
    internal abstract fun bindRecordFragmentInjectorFactory(builder: RecordSubComponent.Builder):
            AndroidInjector.Factory<out Fragment>
}
