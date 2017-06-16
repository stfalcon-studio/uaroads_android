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

package com.stfalcon.new_uaroads_android

/*
 * Created by Anton Bevza on 4/7/17.
 */

import android.app.Activity
import android.app.Service
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.stfalcon.new_uaroads_android.common.injection.components.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasDispatchingActivityInjector
import dagger.android.HasDispatchingServiceInjector
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import javax.inject.Inject

class UaroadsApp : MultiDexApplication(), HasDispatchingActivityInjector, HasDispatchingServiceInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        Realm.init(this)

        Fabric.with(this, Crashlytics())
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInjector;
    }

    override fun serviceInjector(): DispatchingAndroidInjector<Service> {
        return dispatchingServiceInjector
    }
}
