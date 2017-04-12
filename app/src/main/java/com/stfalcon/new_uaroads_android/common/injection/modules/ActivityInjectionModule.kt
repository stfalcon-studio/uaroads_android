package com.stfalcon.new_cookorama_android.injection.module

/*
 * Created by Anton Bevza on 4/5/17.
 */

import android.app.Activity
import com.stfalcon.new_cookorama_android.injection.component.LocationChooserSubComponent
import com.stfalcon.new_cookorama_android.injection.component.MainScreenSubComponent
import com.stfalcon.new_uaroads_android.features.findroute.chooser.LocationChooserActivity
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ActivityInjectionModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivityInjectorFactory(builder: MainScreenSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(LocationChooserActivity::class)
    internal abstract fun bindLocationChooserActivityInjectorFactory(builder: LocationChooserSubComponent.Builder): AndroidInjector.Factory<out Activity>
}