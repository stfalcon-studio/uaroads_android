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

package com.stfalcon.new_uaroads_android.common.ui.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import java.util.*

abstract class BaseListAdapter<T> : RecyclerView.Adapter<BaseListAdapter<T>.ViewHolder>() {

    protected var items: MutableList<T> = ArrayList()

    abstract fun getListItemView(context: Context): BaseViewHolder<T>

    fun clearItems() {
        val itemCount = items.size
        items.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    fun addItems(itemsToAdd: List<T>) {
        items.addAll(itemsToAdd)
        notifyItemRangeInserted(0, itemsToAdd.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder = ViewHolder(getListItemView(viewGroup.context))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.view.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(var view: BaseViewHolder<T>) : RecyclerView.ViewHolder(view)

}
