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

package com.stfalcon.new_uaroads_android.features.settings

import com.stfalcon.mvphelper.IPresenter

/*
 * Created by troy379 on 12.04.17.
 */
class SettingsScreenContract {
    interface View {
        fun showSettings(isAuthorized: Boolean, //TODO data class?
                         email: String?,
                         isWifiOnlyEnabled: Boolean,
                         isSendRoutesAutomatically: Boolean,
                         isAutostartEnabled: Boolean,
                         isNotificationEnabled: Boolean,
                         isPitSoundEnabled: Boolean,
                         isAutostartSoundEnabled: Boolean,
                         isDevModeEnabled: Boolean,
                         appVersion: String,
                         userId: String)

        fun onAuthorizationChanged(isAuthorized: Boolean, email: String?)
        fun onDevModeChanged(isEnabled: Boolean)
        fun showError(message: String)
    }

    interface Presenter: IPresenter<View> {
        fun onLoginButtonClicked(email: String)
        fun onSendViaWiFiClicked(isChecked: Boolean)
        fun onPitSoundClicked(isChecked: Boolean)
        fun onAutostartSoundClicked(isChecked: Boolean)
        fun onSendRoutesAutomaticallyClicked(isChecked: Boolean)
        fun onAutoStartClicked(isChecked: Boolean)
        fun onShowNotificationClicked(isChecked: Boolean)
        fun onAppVersionClicked()
        fun onCopyUserIdClicked()
    }
}