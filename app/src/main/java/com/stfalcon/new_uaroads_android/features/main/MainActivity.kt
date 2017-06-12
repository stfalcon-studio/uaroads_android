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

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.stfalcon.mvphelper.MvpActivity
import com.stfalcon.new_uaroads_android.BuildConfig
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.features.intro.IntroActivity
import com.stfalcon.new_uaroads_android.features.main.adapter.MainScreenPagerAdapter
import com.stfalcon.new_uaroads_android.features.settings.SettingsActivity
import com.stfalcon.new_uaroads_android.features.tracks.TracksActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class MainActivity : MvpActivity<MainScreenContract.Presenter, MainScreenContract.View>(),
        MainScreenContract.View {

    override fun getLayoutResId() = R.layout.activity_main

    @Inject lateinit var pagerAdapter: MainScreenPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initPager()
    }

    override fun onResume() {
        super.onResume()
        checkPlayServicesAvailable()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initPager() {
        tabLayout.setupWithViewPager(pager)
        pager.adapter = pagerAdapter
    }

    private fun checkPlayServicesAvailable() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val status = apiAvailability.isGooglePlayServicesAvailable(this)

        if (status != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(status)) {
                apiAvailability.getErrorDialog(this, status, 1).show()
            } else {
                toast(R.string.error_play_services_unavailable)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_setting -> presenter?.onSettingsClicked()
            R.id.action_list -> presenter?.onTracksClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateToTracks() {
        startActivity(TracksActivity.getIntent(this))
    }

    override fun navigateToSettings() {
        startActivity(SettingsActivity.getIntent(this))
    }

    override fun navigateToIntro() {
        startActivity(IntroActivity.getIntent(this))
        finish()
    }

    override fun showChangelog() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(getString(R.string.app_name) + BuildConfig.VERSION_NAME)
        alert.setMessage(getString(R.string.changelog_message))
        alert.setPositiveButton(getString(R.string.settings_login),
                { _, _ -> startActivity(SettingsActivity.getIntent(this)) }).show()
    }
}
