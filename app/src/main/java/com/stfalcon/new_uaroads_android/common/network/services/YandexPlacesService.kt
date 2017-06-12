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

package com.stfalcon.new_uaroads_android.common.network.services

import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.YandexPlacesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Created by Anton Bevza on 4/12/17.
 */
interface YandexPlacesService {
    @GET("http://geo.uaroads.com/1.x/?format=json&results=4&kind=locality")
    fun getGeoPredictions(@Query("geocode") query: String,
                          @Query("latlon") latLng: LatLng?,
                          @Query("lang") lang: String): Observable<YandexPlacesResponse>
}