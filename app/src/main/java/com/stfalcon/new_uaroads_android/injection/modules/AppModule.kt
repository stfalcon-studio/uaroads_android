package com.stfalcon.new_cookorama_android.injection.module

/*
 * Created by Anton Bevza on 4/5/17.
 */

import android.content.Context
import com.stfalcon.new_cookorama_android.injection.component.MainScreenSubComponent
import com.stfalcon.new_uaroads_android.UaroadsApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = arrayOf(MainScreenSubComponent::class))
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: UaroadsApp): Context {
        return application.applicationContext
    }
}
