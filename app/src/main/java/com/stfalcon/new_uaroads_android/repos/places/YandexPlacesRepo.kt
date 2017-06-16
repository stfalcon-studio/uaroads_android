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

package com.stfalcon.new_uaroads_android.repos.places

import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.network.services.YandexPlacesService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/12/17.
 */
class YandexPlacesRepo @Inject constructor(val service: YandexPlacesService) : PlacesRepo {

    override fun getPlacesPredication(query: String, location: LatLng?): Single<MutableList<GeoPlacePrediction>> {
        val locale = Locale.getDefault().toString()
        return service.getGeoPredictions(query, location, locale)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.response.geoObjectCollection.featureMember }
                .flatMapIterable { it }
                .filter { it.place.metaDataProperty.geocoderMetaData.kind != "country" }
                .toList()

    }

}