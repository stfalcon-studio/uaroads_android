package com.stfalcon.new_cookorama_android.injection.component

/*
 * Created by Anton Bevza on 4/5/17.
 */

import com.stfalcon.new_cookorama_android.injection.module.FragmentInjectionModule
import com.stfalcon.new_cookorama_android.injection.module.MainScreenModule
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(MainScreenModule::class,
        FragmentInjectionModule::class))
interface MainScreenSubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()

}
