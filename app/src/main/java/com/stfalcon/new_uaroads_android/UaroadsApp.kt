package com.stfalcon.new_uaroads_android

/*
 * Created by Anton Bevza on 4/7/17.
 */

import android.app.Activity
import android.app.Application
import com.stfalcon.new_cookorama_android.injection.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasDispatchingActivityInjector
import javax.inject.Inject

class UaroadsApp : Application(), HasDispatchingActivityInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInjector;
    }
}
