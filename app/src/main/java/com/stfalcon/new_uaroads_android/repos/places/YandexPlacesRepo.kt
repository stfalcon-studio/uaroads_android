package com.stfalcon.new_uaroads_android.repos.places

import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng
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