package com.stfalcon.new_cookorama_android.injection.module

/*
 * Created by Anton Bevza on 4/5/17.
 */

import android.app.Activity
import com.stfalcon.new_cookorama_android.injection.component.MainScreenSubComponent
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import com.stfalcon.new_uaroads_android.features.main.MainScreenContract
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ActivitiesModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindFeatureActivityInjectorFactory(builder: MainScreenSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds internal abstract fun provideMainScreenView(mainActivity: MainActivity): MainScreenContract.View
}
