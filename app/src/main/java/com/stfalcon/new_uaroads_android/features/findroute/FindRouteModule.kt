package com.stfalcon.new_uaroads_android.features.findroute

import com.stfalcon.new_uaroads_android.features.findroute.FindRouteContract
import com.stfalcon.new_uaroads_android.features.findroute.FindRouteFragment
import dagger.Module
import dagger.Provides

/*
 * Created by Anton Bevza on 4/7/17.
 */
@Module
class FindRouteModule {
    @Provides fun provideView(fragment: FindRouteFragment): FindRouteContract.View {
        return fragment
    }
}