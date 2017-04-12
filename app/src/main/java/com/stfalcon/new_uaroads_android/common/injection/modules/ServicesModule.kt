package com.stfalcon.new_uaroads_android.common.injection.modules

/*
 * Created by Anton Bevza on 4/12/17.
 */

import com.stfalcon.new_uaroads_android.common.network.services.YandexPlacesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServicesModule {
    @Provides
    @Singleton
    fun providePlacesService(retrofit: Retrofit): YandexPlacesService = retrofit.create(YandexPlacesService::class.java)
}
