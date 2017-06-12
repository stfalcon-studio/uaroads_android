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

package com.stfalcon.new_uaroads_android.features.main

import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.data.preferences.Settings
import com.stfalcon.new_uaroads_android.features.autostart.AutoStartJobScheduler
import com.stfalcon.new_uaroads_android.features.tracks.autoupload.AutoUploaderJobScheduler
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
class MainScreenPresenter @Inject constructor(val settings: Settings,
                                      val autoUploaderJobScheduler: AutoUploaderJobScheduler,
                                      val autoStartJobScheduler: AutoStartJobScheduler,
                                      val analyticsManager: AnalyticsManager) :
        Presenter<MainScreenContract.View>(), MainScreenContract.Presenter {

    override fun onViewAttached(view: MainScreenContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        analyticsManager.sendScreen("MainActivity")
        if (settings.isFirstLaunch()) {
            view.navigateToIntro()
            settings.setFirstLaunch(false)
        } else {
            autoUploaderJobScheduler.scheduleAutoUploadingService()
            autoStartJobScheduler.scheduleAutoStartService()
            if (!settings.isChangelogShown()) {
                view.showChangelog()
                settings.setChangelogShown(true)
            }
        }
    }

    override fun onTracksClicked() {
        view?.navigateToTracks()
    }

    override fun onSettingsClicked() {
        view?.navigateToSettings()
    }

}
