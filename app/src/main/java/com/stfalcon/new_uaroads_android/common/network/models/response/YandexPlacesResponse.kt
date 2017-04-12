package com.stfalcon.new_uaroads_android.common.network.models.response

import com.google.gson.annotations.SerializedName

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

