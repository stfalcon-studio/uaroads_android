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

package com.stfalcon.new_uaroads_android.features.record

import android.location.Location
import com.stfalcon.mvphelper.IPresenter
import com.stfalcon.new_uaroads_android.features.record.service.IRecordService

/*
 * Created by Anton Bevza on 4/19/17.
 */

class RecordContract {
    interface View {
        fun showLastSessionDistance(distance: Int)
        fun showProcessedDistance(distance: Int)
        fun showCurrentSessionDistance(distance: Int)
        fun showForceValue(value: Double)
        fun showIdleState()
        fun showRecordState()
        fun showPauseState()
        fun showGpsSignalStatus(location: Location)
        fun startRecordService()
        fun showError(error: String?)
        fun showAuthState(isAuth: Boolean)
        fun navigateToSettings()
        fun openHomePage()
    }

    interface Presenter: IPresenter<View> {
        fun onStartButtonClicked()
        fun onPauseButtonClicked()
        fun onStopButtonClicked()
        fun onContinueButtonClicked()
        fun onServiceConnected(service: IRecordService)
        fun onServiceDisconnected()
        fun onLoginButtonClicked()
        fun onCopyrightClicked()
    }
}
