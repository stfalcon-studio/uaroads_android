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

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.stfalcon.mvphelper.MvpActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SettingsActivity :
        MvpActivity<SettingsScreenContract.Presenter, SettingsScreenContract.View>(),
        SettingsScreenContract.View,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    companion object {
        fun getIntent(context: Context) = context.intentFor<SettingsActivity>()
    }

    override fun getLayoutResId(): Int = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        btnLogin.setOnClickListener(this)
        switchWifiOnly.setOnCheckedChangeListener(this)
        switchPitSound.setOnCheckedChangeListener(this)
        switchAutostartSound.setOnCheckedChangeListener(this)
        switchAutoSend.setOnCheckedChangeListener(this)
        switchAutostart.setOnCheckedChangeListener(this)
        switchShowNotification.setOnCheckedChangeListener(this)
        containerAppVersion.setOnClickListener(this)
        btnCopy.setOnClickListener(this)
    }

    override fun showSettings(isAuthorized: Boolean, email: String?, isWifiOnlyEnabled: Boolean,
                              isSendRoutesAutomatically: Boolean, isAutostartEnabled: Boolean,
                              isNotificationEnabled: Boolean, isPitSoundEnabled: Boolean,
                              isAutostartSoundEnabled: Boolean, isDevModeEnabled: Boolean,
                              appVersion: String, userId: String) {

        onAuthorizationChanged(isAuthorized, email)
        onDevModeChanged(isDevModeEnabled)

        switchWifiOnly.isChecked = isWifiOnlyEnabled
        switchPitSound.isChecked = isPitSoundEnabled
        switchAutostartSound.isChecked = isAutostartSoundEnabled
        switchAutoSend.isChecked = isSendRoutesAutomatically
        switchAutostart.isChecked = isAutostartEnabled
        switchShowNotification.isChecked = isNotificationEnabled

        tvAppVersion.text = appVersion
        tvUserId.text = userId
    }

    override fun onAuthorizationChanged(isAuthorized: Boolean, email: String?) {
        etEmail.setText(email ?: "")
        etEmail.isEnabled = !isAuthorized
        btnLogin.setText(if (isAuthorized) R.string.settings_logout else R.string.settings_login)
    }

    override fun onDevModeChanged(isEnabled: Boolean) {
        if (isEnabled) {
            switchPitSound.showView()
            switchAutostartSound.showView()
            dividerPitSound.showView()
            dividerAutostartSound.showView()
        } else {
            switchPitSound.hideView()
            switchAutostartSound.hideView()
            dividerPitSound.hideView()
            dividerAutostartSound.hideView()
        }
    }

    override fun onCheckedChanged(button: CompoundButton?, value: Boolean) {
        when (button?.id) {
            R.id.switchWifiOnly -> presenter?.onSendViaWiFiClicked(button.isChecked)
            R.id.switchPitSound -> presenter?.onPitSoundClicked(button.isChecked)
            R.id.switchAutostartSound -> presenter?.onAutostartSoundClicked(button.isChecked)
            R.id.switchAutoSend -> presenter?.onSendRoutesAutomaticallyClicked(button.isChecked)
            R.id.switchAutostart -> presenter?.onAutoStartClicked(button.isChecked)
            R.id.switchShowNotification -> presenter?.onShowNotificationClicked(button.isChecked)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> presenter?.onLoginButtonClicked(etEmail.text.toString())
            R.id.containerAppVersion -> presenter?.onAppVersionClicked()
            R.id.btnCopy -> presenter?.onCopyUserIdClicked()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun showError(message: String) {
        toast(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        //presenter.destroy()
    }
}
