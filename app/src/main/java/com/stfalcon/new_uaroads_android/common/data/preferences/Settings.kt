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

/*
 * Created by troy379 on 13.04.17.
 */
interface Settings {

    val PREFERENCES_NAME: String get() = "SHARED_PREFERENCES"

    val IS_AUTHORIZED: String get() = "IS_AUTHORIZED"
    val USER_EMAIL: String get() = "USER_EMAIL"

    val SEND_WIFI_ONLY: String get() = "WIFI_ONLY"
    val SEND_ROUTES_AUTOMATICALLY: String get() = "SEND_ROUTES_AUTOMATICALLY"
    val USE_AUTOSTART: String get() = "USE_AUTOSTART"
    val SHOW_NOTIFICATION: String get() = "SHOW_NOTIFICATION"

    val PLAY_SOUND_ON_PIT: String get() = "PLAY_SOUND_ON_PIT"
    val PLAY_SOUND_ON_AUTOSTART: String get() = "PLAY_SOUND_ON_AUTOSTART"

    val IS_DEV_MODE: String get() = "IS_DEV_MODE"

    val IS_FIRST_LAUNCH: String get() = "IS_FIRST_LAUNCH"
    val IS_CHANGELOG_SHOWN: String get() = "IS_CHANGELOG_SHOWN"

    val TOTAL_DISTANCE: String get() = "TOTAL_DISTANCE"
    val LAST_DISTANCE: String get() = "LAST_DISTANCE"

    fun setAuthorized(isAuthorized: Boolean)
    fun isAuthorized(): Boolean

    fun setUserEmail(email: String?)
    fun getUserEmail(): String?

    fun enableWifiOnlyMode(enabled: Boolean)
    fun isWifiOnlyEnabled(): Boolean

    fun sendRoutesAutomatically(enabled: Boolean)
    fun isSendRoutesAutomatically(): Boolean

    fun enableAutostart(enabled: Boolean)
    fun isAutostartEnabled(): Boolean

    fun enableNotification(enabled: Boolean)
    fun isNotificationEnabled(): Boolean

    fun enablePitSound(enabled: Boolean)
    fun isPitSoundEnabled(): Boolean

    fun enableAutostartSound(enabled: Boolean)
    fun isAutostartSoundEnabled(): Boolean

    fun enableDevMode(enabled: Boolean)
    fun isDevModeEnabled(): Boolean

    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean

    fun setChangelogShown(isShown: Boolean)
    fun isChangelogShown(): Boolean

    fun setLastDistance(lastDistance: Float)
    fun getLastDistance() : Float
}