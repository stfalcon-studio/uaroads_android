package com.stfalcon.new_uaroads_android.features.findroute.chooser.adapter

import android.content.Context
import android.view.View
import com.michaldrabik.kotlintest.ui.base.list.BaseViewHolder
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import kotlinx.android.synthetic.main.item_place.view.*

/*
 * Created by Anton Bevza on 4/12/17.
 */
class PlaceViewHolder(context: Context, val onClickItem: ((view: View, item: GeoPlacePrediction) -> Unit)?)
    : BaseViewHolder<GeoPlacePrediction>(context) {
    override fun layoutResId() = R.layout.item_place

    override fun bind(item: GeoPlacePrediction) {
        tvPlaceTitle.text = item.place.name
        tvPlaceSubtitle.text = item.place.description
        containerItem.setOnClickListener {
            onClickItem?.invoke(it, item)
        }
    }
}