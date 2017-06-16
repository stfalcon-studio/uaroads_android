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

import com.stfalcon.mvphelper.IPresenter
import com.stfalcon.new_uaroads_android.common.database.models.TrackRealm
import com.stfalcon.new_uaroads_android.features.tracks.service.ITracksUploadService
import io.realm.RealmResults

/*
 * Created by Anton Bevza on 4/26/17.
 */
class TracksContract {
    interface View {

        fun showTrackList(tracks: RealmResults<TrackRealm>)
        fun startUploadingService()
        fun showConnectionError()
    }

    interface Presenter: IPresenter<View> {
        fun onDeleteTrackClicked(trackId: String)
        fun onSendTrackClicked(trackId: String)
        fun onServiceConnected(service: ITracksUploadService)
        fun onServiceDisconnected()
    }
}