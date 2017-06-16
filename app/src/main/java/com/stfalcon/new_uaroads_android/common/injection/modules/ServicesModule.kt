/*
 * Copyright (c) 2017 stfalcon.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.stfalcon.new_uaroads_android.common.injection.modules

/*
 * Created by Anton Bevza on 4/12/17.
 */

import com.stfalcon.new_uaroads_android.common.network.services.RoutesService
import com.stfalcon.new_uaroads_android.common.network.services.TracksService
import com.stfalcon.new_uaroads_android.common.network.services.UserService
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

    @Provides
    @Singleton
    fun provideRoutesService(retrofit: Retrofit): RoutesService = retrofit.create(RoutesService::class.java)

    @Provides
    @Singleton
    fun provideTracksService(retrofit: Retrofit): TracksService = retrofit.create(TracksService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)
}
