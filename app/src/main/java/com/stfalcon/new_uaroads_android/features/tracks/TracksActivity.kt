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

package com.stfalcon.new_uaroads_android.features.tracks

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import com.stfalcon.mvphelper.MvpActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.database.models.TrackRealm
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import com.stfalcon.new_uaroads_android.features.tracks.adapter.TracksListAdapter
import com.stfalcon.new_uaroads_android.features.tracks.service.TracksUploadService
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_tracks.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

/*
 * Created by Anton Bevza on 4/26/17.
 */
class TracksActivity : MvpActivity<TracksContract.Presenter, TracksContract.View>(),
        TracksContract.View, ServiceConnection {

    companion object {
        fun getIntent(context: Context) = context.intentFor<TracksActivity>()
    }

    private var isBound: Boolean = false

    override fun getLayoutResId() = R.layout.activity_tracks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        isBound = bindService(intentFor<TracksUploadService>(), this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(this)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun showTrackList(tracks: RealmResults<TrackRealm>) {
        val adapter = TracksListAdapter(this, tracks)
        if (tracks.isEmpty()) emptyState.showView() else emptyState.hideView()
        adapter.onSendButtonClick = { presenter?.onSendTrackClicked(it) }
        adapter.onDeleteButtonClick = {
            presenter?.onDeleteTrackClicked(it)
            if (adapter.itemCount == 0) emptyState.showView() else emptyState.hideView()
        }
        tracksRecyclerView.adapter = adapter
        tracksRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun showConnectionError() {
        toast(R.string.error_connection)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        presenter?.onServiceConnected((service as TracksUploadService.LocalBinder).service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        presenter?.onServiceDisconnected()
    }

    override fun startUploadingService() {
        startService(intentFor<TracksUploadService>())
    }
}