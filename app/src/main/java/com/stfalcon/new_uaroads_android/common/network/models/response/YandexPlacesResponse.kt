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

package com.stfalcon.new_uaroads_android.common.network.models.response

import com.google.gson.annotations.SerializedName
import com.stfalcon.new_uaroads_android.common.network.models.LatLng

/*
 * Created by Anton Bevza on 4/12/17.
 */
data class YandexPlacesResponse(
        @SerializedName("response") val response: Response
)

data class Response(
        @SerializedName("GeoObjectCollection") val geoObjectCollection: GeoObjectCollection
)

data class GeoObjectCollection(
        @SerializedName("featureMember") val featureMember: List<GeoPlacePrediction>
)

data class GeoPlacePrediction(
        @SerializedName("GeoObject") var place: GeoObject
)

data class GeoObject(
        @SerializedName("description") val description: String,
        @SerializedName("name") val name: String,
        @SerializedName("Point") val point: PlacePoint,
        @SerializedName("metaDataProperty") val metaDataProperty: MetaDataProperty
)

data class PlacePoint(@SerializedName("pos") var pos: String) {
    val position get() = LatLng(pos)
}

data class MetaDataProperty(@SerializedName("GeocoderMetaData") val geocoderMetaData: GeocoderMetaData)

data class GeocoderMetaData(@SerializedName("kind") val kind: String)

