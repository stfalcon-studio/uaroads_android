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

package com.stfalcon.new_uaroads_android.common.data.preferences

import android.content.Context
import android.content.SharedPreferences

/*
 * Created by troy379 on 13.04.17.
 */
class SettingsImpl(val context: Context) : Settings {

    override fun setAuthorized(isAuthorized: Boolean) {
        getEditor().putBoolean(IS_AUTHORIZED, isAuthorized).apply()
    }

    override fun isAuthorized(): Boolean = getReader().getBoolean(IS_AUTHORIZED, false)

    override fun setUserEmail(email: String?) {
        getEditor().putString(USER_EMAIL, email).apply()
    }

    override fun getUserEmail(): String? = getReader().getString(USER_EMAIL, null)

    override fun enableWifiOnlyMode(enabled: Boolean) {
        getEditor().putBoolean(SEND_WIFI_ONLY, enabled).apply()
    }

    override fun isWifiOnlyEnabled(): Boolean = getReader().getBoolean(SEND_WIFI_ONLY, true)

    override fun sendRoutesAutomatically(enabled: Boolean) {
        getEditor().putBoolean(SEND_ROUTES_AUTOMATICALLY, enabled).apply()
    }

    override fun isSendRoutesAutomatically(): Boolean
            = getReader().getBoolean(SEND_ROUTES_AUTOMATICALLY, true)

    override fun enableAutostart(enabled: Boolean) {
        getEditor().putBoolean(USE_AUTOSTART, enabled).apply()
    }

    override fun isAutostartEnabled(): Boolean = getReader().getBoolean(USE_AUTOSTART, true)

    override fun enableNotification(enabled: Boolean) {
        getEditor().putBoolean(SHOW_NOTIFICATION, enabled).apply()
    }

    override fun isNotificationEnabled(): Boolean = getReader().getBoolean(SHOW_NOTIFICATION, false)

    override fun enablePitSound(enabled: Boolean) {
        getEditor().putBoolean(PLAY_SOUND_ON_PIT, enabled).apply()
    }

    override fun isPitSoundEnabled(): Boolean = getReader().getBoolean(PLAY_SOUND_ON_PIT, false)

    override fun enableAutostartSound(enabled: Boolean) {
        getEditor().putBoolean(PLAY_SOUND_ON_AUTOSTART, enabled).apply()
    }

    override fun isAutostartSoundEnabled(): Boolean = getReader().getBoolean(PLAY_SOUND_ON_AUTOSTART, false)

    override fun enableDevMode(enabled: Boolean) {
        getEditor().putBoolean(IS_DEV_MODE, enabled).apply()
    }

    override fun isDevModeEnabled(): Boolean = getReader().getBoolean(IS_DEV_MODE, false)

    override fun isFirstLaunch(): Boolean {
        return getReader().getBoolean(IS_FIRST_LAUNCH, true)
    }

    override fun setFirstLaunch(isFirst: Boolean) {
        getEditor().putBoolean(IS_FIRST_LAUNCH, isFirst).apply()
    }

    override fun setChangelogShown(isShown: Boolean) {
        getEditor().putBoolean(IS_CHANGELOG_SHOWN, isShown).apply()    }

    override fun isChangelogShown(): Boolean {
        return getReader().getBoolean(IS_CHANGELOG_SHOWN, false)
    }

    override fun setLastDistance(lastDistance: Float) {
        getEditor().putFloat(LAST_DISTANCE, lastDistance).apply()
    }

    override fun getLastDistance(): Float {
        return getReader().getFloat(LAST_DISTANCE, 0f)
    }

    private fun getReader(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private fun getEditor(): SharedPreferences.Editor {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
    }
}