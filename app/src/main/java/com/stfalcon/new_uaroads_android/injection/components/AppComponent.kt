package com.stfalcon.new_cookorama_android.injection.component

import com.stfalcon.new_cookorama_android.injection.module.ActivityInjectionModule
import com.stfalcon.new_cookorama_android.injection.module.AppModule
import com.stfalcon.new_cookorama_android.injection.module.NetworkModule
import com.stfalcon.new_uaroads_android.UaroadsApp
import com.stfalcon.new_uaroads_android.injection.modules.LocationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/*
 * Created by Anton Bevza on 3/28/17.
 */
@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class,
        ActivityInjectionModule::class, NetworkModule::class, LocationModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: UaroadsApp): Builder
        fun build(): AppComponent
    }

    fun inject(app: UaroadsApp)

}