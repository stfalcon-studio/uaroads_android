package com.stfalcon.new_cookorama_android.injection.module

/*
 * Created by Anton Bevza on 4/7/17.
 */

import com.stfalcon.new_uaroads_android.common.network.services.YandexPlacesService
import com.stfalcon.new_uaroads_android.features.findroute.chooser.LocationChooserActivity
import com.stfalcon.new_uaroads_android.features.findroute.chooser.LocationChooserContract
import com.stfalcon.new_uaroads_android.repos.places.PlacesRepo
import com.stfalcon.new_uaroads_android.repos.places.YandexPlacesRepo
import dagger.Module
import dagger.Provides

@Module
class LocationChooserModule {

    @Provides fun provideView(activity: LocationChooserActivity): LocationChooserContract.View {
        return activity
    }

    @Provides fun provideLocation(activity: LocationChooserActivity) = activity.location

    @Provides fun providePlacesRepo(service: YandexPlacesService): PlacesRepo = YandexPlacesRepo(service)

}
