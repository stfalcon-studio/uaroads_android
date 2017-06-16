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

package com.stfalcon.new_uaroads_android.features.tracks.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.database.models.TrackRealm
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import org.jetbrains.anko.find

/*
 * Created by Anton Bevza on 4/27/17.
 */
class TracksListAdapter(val context: Context, realmResults: RealmResults<TrackRealm>)
    : RealmRecyclerViewAdapter<TrackRealm, TracksListAdapter.ViewHolder>(realmResults, true) {
    var onDeleteButtonClick: ((trackId: String) -> Unit)? = null
    var onSendButtonClick: ((trackId: String) -> Unit)? = null

    val statusImages = mapOf(
            TrackRealm.STATUS_SENT to R.drawable.ic_session_status_sent,
            TrackRealm.STATUS_NOT_SENT to R.drawable.ic_session_status_error,
            TrackRealm.STATUS_IS_SENDING to R.drawable.ic_session_status_waiting)


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = getItem(position)
        item?.let { track ->
            holder?.tvTrackComment?.text = track.comment
            holder?.tvTrackDistance?.text = context.getString(R.string.tracks_distance, track.distance)
            holder?.ivStatus?.setImageResource(statusImages.getValue(track.status))
            when (track.status) {
                TrackRealm.STATUS_SENT -> {
                    holder?.progress?.hideView()
                    holder?.btnSend?.hideView()

                }
                TrackRealm.STATUS_IS_SENDING -> {
                    holder?.progress?.showView()
                    holder?.btnSend?.hideView()
                }
                TrackRealm.STATUS_NOT_SENT -> {
                    holder?.progress?.hideView()
                    holder?.btnSend?.showView()
                }
            }

            holder?.btnDelete?.setOnClickListener { onDeleteButtonClick?.invoke(track.id) }
            holder?.btnSend?.setOnClickListener { onSendButtonClick?.invoke(track.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_track, parent, false))
    }

    class ViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        val ivStatus = rootView.find<ImageView>(R.id.ivStatus)
        val tvTrackComment = rootView.find<TextView>(R.id.tvTrackComment)
        val tvTrackDistance = rootView.find<TextView>(R.id.tvTrackDistance)
        val btnSend = rootView.find<ImageButton>(R.id.btnSend)
        val btnDelete = rootView.find<ImageButton>(R.id.btnDelete)
        val progress = rootView.find<View>(R.id.progressBar)
    }
}