package com.stfalcon.new_cookorama_android.injection.component

/*
 * Created by Anton Bevza on 4/5/17.
 */

import com.stfalcon.new_cookorama_android.injection.module.LocationChooserModule
import com.stfalcon.new_uaroads_android.features.findroute.chooser.LocationChooserActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(LocationChooserModule::class))
interface LocationChooserSubComponent : AndroidInjector<LocationChooserActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<LocationChooserActivity>()

}
