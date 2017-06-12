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

package com.stfalcon.new_uaroads_android.common.analytics

import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker


/*
 * Created by Anton Bevza on 5/13/17.
 */
class AnalyticsManagerImpl(val tracker: Tracker) : AnalyticsManager {

    val CATEGORY_NAVIGATION = "Navigation"
    val CATEGORY_RECORD = "Record"
    val CATEGORY_SETTING = "Settings"
    val CATEGORY_TRACKS = "Tracks"

    val ACTION_SEARCH = "search"
    val ACTION_NAVIGATE = "navigate"
    val ACTION_START_MANUAL_RECORD = "startManualRecord"
    val ACTION_STOP_MANUAL_RECORD = "stopManualRecord"
    val ACTION_PAUSE_MANUAL_RECORD = "pauseManualRecord"
    val ACTION_START_AUTO_RECORD = "startAutoRecord"
    val ACTION_STOP_AUTO_RECORD = "stopAutoRecord"
    val ACTION_SETTING_PIT_SOUND = "Pit Sound"
    val ACTION_SETTING_AUTO_START_SOUND = "Auto Start Sound"
    val ACTION_SETTING_SEND_ONLY_WIFI = "Send Only WiFi"
    val ACTION_SETTING_AUTO_RECORD = "Auto Record"
    val ACTION_SETTING_AUTO_SENDING = "Auto Record NOTIFY"
    val ACTION_TRACK_MANUAL_SENT = "manual sent"
    val ACTION_TRACK_AUTO_SENT = "auto sent"

    override fun sendScreen(screenName: String) {
        tracker.setScreenName(screenName)
        tracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    override fun sendSearchAction(from: String, to: String) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_NAVIGATION)
                .setAction(ACTION_SEARCH)
                .setLabel("$from->$to")
                .build())
    }

    override fun sendNavigationAction() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_NAVIGATION)
                .setAction(ACTION_NAVIGATE)
                .build())
    }

    override fun sendStartManualRecord() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_RECORD)
                .setAction(ACTION_START_MANUAL_RECORD)
                .build())
    }

    override fun sendStopManualRecord() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_RECORD)
                .setAction(ACTION_STOP_MANUAL_RECORD)
                .build())
    }

    override fun sendPauseManualRecord() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_RECORD)
                .setAction(ACTION_PAUSE_MANUAL_RECORD)
                .build())
    }

    override fun sendStartAutoRecord() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_RECORD)
                .setAction(ACTION_START_AUTO_RECORD)
                .build())
    }

    override fun sendStopAutoRecord() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_RECORD)
                .setAction(ACTION_STOP_AUTO_RECORD)
                .build())
    }

    override fun sendSettingPitSound(checked: Boolean) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_SETTING)
                .setAction(ACTION_SETTING_PIT_SOUND)
                .setLabel(checked.toString())
                .build())
    }

    override fun sendSettingAutoStartSound(checked: Boolean) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_SETTING)
                .setAction(ACTION_SETTING_AUTO_START_SOUND)
                .setLabel(checked.toString())
                .build())
    }

    override fun sendSettingSendingOnlyWIfi(checked: Boolean) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_SETTING)
                .setAction(ACTION_SETTING_SEND_ONLY_WIFI)
                .setLabel(checked.toString())
                .build())
    }

    override fun sendSettingAutoSending(checked: Boolean) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_SETTING)
                .setAction(ACTION_SETTING_AUTO_SENDING)
                .setLabel(checked.toString())
                .build())
    }

    override fun sendSettingAutoRecord(checked: Boolean) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_SETTING)
                .setAction(ACTION_SETTING_AUTO_RECORD)
                .setLabel(checked.toString())
                .build())
    }

    override fun sendTrackAutoSent() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_TRACKS)
                .setAction(ACTION_TRACK_AUTO_SENT)
                .build())
    }

    override fun sendTrackManualSent() {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(CATEGORY_TRACKS)
                .setAction(ACTION_TRACK_MANUAL_SENT)
                .build())
    }

}