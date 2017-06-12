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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Patterns
import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.data.preferences.Settings
import com.stfalcon.new_uaroads_android.common.injection.InjectionConsts
import com.stfalcon.new_uaroads_android.features.autostart.AutoStartJobScheduler
import com.stfalcon.new_uaroads_android.features.tracks.autoupload.AutoUploaderJobScheduler
import com.stfalcon.new_uaroads_android.repos.tracks.TracksRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.toast
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by troy379 on 12.04.17.
 */
class SettingsScreenPresenter @Inject constructor(
        val context: Context,
        @Named(InjectionConsts.NAME_USER_ID) val userId: String,
        @Named(InjectionConsts.NAME_APP_VERSION) val appVersion: String,
        @Named(InjectionConsts.NAME_ACCOUNT_EMAIL) val accountEmail: String,
        @Named(InjectionConsts.NAME_SENSOR_INFO) val sensorInfo: String,
        val settings: Settings,
        val tracksRepo: TracksRepo,
        val autoUploaderJobScheduler: AutoUploaderJobScheduler,
        val autoStartJobScheduler: AutoStartJobScheduler,
        val analyticsManager: AnalyticsManager)
    : Presenter<SettingsScreenContract.View>(), SettingsScreenContract.Presenter {

    private var developerCounter = 0

    override fun onViewAttached(view: SettingsScreenContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        view.showSettings(
                settings.isAuthorized(),
                settings.getUserEmail() ?: accountEmail,
                settings.isWifiOnlyEnabled(),
                settings.isSendRoutesAutomatically(),
                settings.isAutostartEnabled(),
                settings.isNotificationEnabled(),
                settings.isPitSoundEnabled(),
                settings.isAutostartSoundEnabled(),
                settings.isDevModeEnabled(),
                appVersion,
                userId)
        view.onAuthorizationChanged(settings.isAuthorized(), settings.getUserEmail() ?: accountEmail)

        analyticsManager.sendScreen("SettingActivity")
    }

    override fun onLoginButtonClicked(email: String) {
        if (checkIsEmailValid(email)) {
            if (!settings.isAuthorized())
                disposables.add(tracksRepo.sendDeviceInfo(email, sensorInfo, userId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    settings.setUserEmail(email)
                                    settings.setAuthorized(true)
                                    view?.onAuthorizationChanged(true, email)
                                },
                                {
                                    it.message?.let { view?.showError(it) }
                                }
                        ))
            else {
                settings.setUserEmail(null)
                settings.setAuthorized(false)
                view?.onAuthorizationChanged(false, email)
            }
        } else {
            context.toast(R.string.settings_error_email)
        }
    }

    override fun onSendViaWiFiClicked(isChecked: Boolean) {
        settings.enableWifiOnlyMode(isChecked)
        analyticsManager.sendSettingSendingOnlyWIfi(isChecked)
    }

    override fun onPitSoundClicked(isChecked: Boolean) {
        settings.enablePitSound(isChecked)
        analyticsManager.sendSettingPitSound(isChecked)
    }

    override fun onAutostartSoundClicked(isChecked: Boolean) {
        settings.enableAutostartSound(isChecked)
        analyticsManager.sendSettingAutoStartSound(isChecked)
    }

    override fun onSendRoutesAutomaticallyClicked(isChecked: Boolean) {
        settings.sendRoutesAutomatically(isChecked)
        if (isChecked) {
            autoUploaderJobScheduler.scheduleAutoUploadingService()
        } else {
            autoUploaderJobScheduler.cancelAll()
        }
        analyticsManager.sendSettingAutoSending(isChecked)
    }

    override fun onAutoStartClicked(isChecked: Boolean) {
        settings.enableAutostart(isChecked)
        if (isChecked) {
            autoStartJobScheduler.scheduleAutoStartService()
        } else {
            autoStartJobScheduler.cancelAll()
        }
        analyticsManager.sendSettingAutoRecord(isChecked)
    }

    override fun onShowNotificationClicked(isChecked: Boolean) {
        settings.enableNotification(isChecked)
    }

    override fun onAppVersionClicked() {
        developerCounter++
        if (settings.isDevModeEnabled() && developerCounter == 3) {
            developerCounter = 0
            context.toast(R.string.settings_dev_mode_disabled)
            settings.enableDevMode(false)
            view?.onDevModeChanged(false)
        } else if (developerCounter == 7) {
            developerCounter = 0
            context.toast(R.string.settings_dev_mode_enabled)
            settings.enableDevMode(true)
            view?.onDevModeChanged(true)
        }
    }

    override fun onCopyUserIdClicked() {
        copyToClipboard(userId)
        context.toast(R.string.settings_user_id_copied)
    }

    private fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(text, text)
        clipboard.primaryClip = clip
    }

    private fun checkIsEmailValid(email: String?)
            = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}