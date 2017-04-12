package com.stfalcon.new_uaroads_android.common.network.services

import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.YandexPlacesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Created by Anton Bevza on 4/12/17.
 */
interface YandexPlacesService {
    @GET("https://geocode-maps.yandex.ru/1.x/?format=json&results=4&kind=locality")
    fun getGeoPredictions(@Query("geocode") query: String,
                          @Query("latlon") latLng: LatLng?,
                          @Query("lang") lang: String): Observable<YandexPlacesResponse>
}