package com.stfalcon.new_uaroads_android.features.findroute.chooser.adapter

import android.content.Context
import android.view.View
import com.michaldrabik.kotlintest.ui.base.list.BaseListAdapter
import com.michaldrabik.kotlintest.ui.base.list.BaseViewHolder
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/12/17.
 */
class PlacesListAdapter @Inject constructor() : BaseListAdapter<GeoPlacePrediction>() {
    var onClickItem: ((view: View, item: GeoPlacePrediction) -> Unit)? = null

    override fun getListItemView(context: Context): BaseViewHolder<GeoPlacePrediction> {
        return PlaceViewHolder(context, onClickItem)
    }

}