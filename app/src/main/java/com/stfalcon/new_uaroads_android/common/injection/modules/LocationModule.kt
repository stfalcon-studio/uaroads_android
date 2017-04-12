package com.stfalcon.new_uaroads_android.common.injection.modules

import android.content.Context
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

/*
 * Created by Anton Bevza on 4/10/17.
 */
@Module
class LocationModule {

    @Provides
    fun provideRxLocation(context: Context): RxLocation {
        val rxLocation = RxLocation(context)
        rxLocation.setDefaultTimeout(15, TimeUnit.SECONDS)
        return rxLocation
    }

}