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

import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.database.RealmService
import com.stfalcon.new_uaroads_android.features.tracks.service.ITracksUploadService
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/26/17.
 */
class TracksPresenter @Inject constructor(val realmService: RealmService,
                                          val analyticsManager: AnalyticsManager) :
        Presenter<TracksContract.View>(), TracksContract.Presenter {
    var service: ITracksUploadService? = null

    override fun onViewAttached(view: TracksContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        view.showTrackList(realmService.getAllRecordedTracks())
        analyticsManager.sendScreen("TrackListActivity")
    }

    override fun onDeleteTrackClicked(trackId: String) {
        realmService.deleteTrack(trackId)
    }

    override fun onSendTrackClicked(trackId: String) {
        view?.startUploadingService()
        service?.sendTrack(trackId)
                ?.subscribe({ analyticsManager.sendTrackManualSent() }, { view?.showConnectionError() })
    }

    override fun onServiceConnected(service: ITracksUploadService) {
        this.service = service
    }

    override fun onServiceDisconnected() {
        service = null
    }


}