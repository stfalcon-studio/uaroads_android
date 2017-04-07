package com.stfalcon.new_cookorama_android.injection.module

/*
 * Created by Anton Bevza on 4/5/17.
 */

import android.support.v4.app.Fragment
import com.stfalcon.new_cookorama_android.injection.component.FindRouteSubComponent
import com.stfalcon.new_uaroads_android.features.findroute.FindRouteFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(FindRouteSubComponent::class))
abstract class FragmentInjectionModule {
    @Binds
    @IntoMap
    @FragmentKey(FindRouteFragment::class)
    internal abstract fun bindYourFragmentInjectorFactory(builder: FindRouteSubComponent.Builder): AndroidInjector.Factory<out Fragment>
}
