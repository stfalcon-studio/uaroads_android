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

package com.stfalcon.new_uaroads_android.features.places.adapter

import android.content.Context
import android.view.View
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.ui.list.BaseViewHolder
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