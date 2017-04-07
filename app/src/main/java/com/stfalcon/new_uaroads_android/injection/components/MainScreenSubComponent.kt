package com.stfalcon.new_cookorama_android.injection.component

/*
 * Created by Anton Bevza on 4/5/17.
 */

import com.stfalcon.new_cookorama_android.injection.scope.PerActivity
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@PerActivity
@Subcomponent(modules = arrayOf())
interface MainScreenSubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()

}
