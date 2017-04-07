package com.stfalcon.new_cookorama_android.injection.component

import com.stfalcon.new_uaroads_android.features.findroute.FindRouteFragment
import com.stfalcon.new_uaroads_android.injection.modules.FindRouteModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

/*
 * Created by Anton Bevza on 4/5/17.
 */
@Subcomponent(modules = arrayOf(FindRouteModule::class))
interface FindRouteSubComponent : AndroidInjector<FindRouteFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FindRouteFragment>()
}