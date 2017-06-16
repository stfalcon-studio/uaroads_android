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

package com.stfalcon.new_uaroads_android.common.injection.modules

/*
 * Created by Anton Bevza on 4/5/17.
 */

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.provider.Settings
import com.stfalcon.new_uaroads_android.UaroadsApp
import com.stfalcon.new_uaroads_android.common.injection.InjectionConsts
import com.stfalcon.new_uaroads_android.features.autostart.AutoStartServiceSubComponent
import com.stfalcon.new_uaroads_android.features.main.MainScreenSubComponent
import com.stfalcon.new_uaroads_android.features.places.BestRouteSubComponent
import com.stfalcon.new_uaroads_android.features.places.LocationChooserSubComponent
import com.stfalcon.new_uaroads_android.features.record.service.RecordServiceSubComponent
import com.stfalcon.new_uaroads_android.features.settings.SettingsScreenSubComponent
import com.stfalcon.new_uaroads_android.features.settings.TracksSubComponent
import com.stfalcon.new_uaroads_android.features.tracks.autoupload.AutoUploadServiceSubComponent
import com.stfalcon.new_uaroads_android.features.tracks.service.TracksServiceSubComponent
import com.stfalcon.new_uaroads_android.utils.DataUtils
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(subcomponents = arrayOf(
        MainScreenSubComponent::class,
        LocationChooserSubComponent::class,
        BestRouteSubComponent::class,
        SettingsScreenSubComponent::class,
        RecordServiceSubComponent::class,
        TracksServiceSubComponent::class,
        AutoUploadServiceSubComponent::class,
        AutoStartServiceSubComponent::class,
        TracksSubComponent::class)
)
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: UaroadsApp): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides @Named(InjectionConsts.NAME_USER_ID)
    internal fun provideUserId(application: UaroadsApp): String {
        return Settings.Secure.getString(
                application.contentResolver,
                android.provider.Settings.Secure.ANDROID_ID)
    }

    @Singleton
    @Provides @Named(InjectionConsts.NAME_APP_VERSION)
    internal fun provideAppVersion(application: UaroadsApp): String {
        try {
            return application.packageManager.getPackageInfo(application.packageName, 0).versionName
        } catch (ignore: Exception) {
            return ""
        }
    }

    @Singleton
    @Provides @Named(InjectionConsts.NAME_ACCOUNT_EMAIL)
    internal fun provideAccountEmail(application: UaroadsApp): String {
        val accountManager = AccountManager.get(application)
        val account = getAccount(accountManager) ?: return ""
        return account.name
    }

    @Singleton
    @Provides
    internal fun provideDataUtils(): DataUtils {
        return DataUtils()
    }

    private fun getAccount(accountManager: AccountManager): Account? {
        val accounts = accountManager.getAccountsByType("com.google")
        return if (accounts.isNotEmpty()) accounts[0] else null
    }

    @Singleton
    @Provides  @Named(InjectionConsts.NAME_SENSOR_INFO)
    internal fun getSensorInfo(application: UaroadsApp): String {
        val sensorManager = application.getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var sensorInfo = StringBuilder()
        sensor?.let {
            sensorInfo.append("TYPE_ACCELEROMETER - ")
                    .append(it.name)
                    .append("\n Version - ")
                    .append(it.version)
                    .append("\n Type - ${it.type}")
                    .append("\n Vendor - ${it.vendor}")
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sensorInfo.append("\n FifoMaxEventCount -  ${it.fifoMaxEventCount}")
                        .append("\n FifoReservedEventCount - ${it.fifoReservedEventCount}")
            }
            sensorInfo.append("\n MaximumRange - ${it.maximumRange}")
                    .append("\n MinDelay - ${it.minDelay} microsecond")
                    .append("\n Power - ${it.power}")
                    .append("\n Resolution - ${it.resolution}")
        }
        return sensorInfo.toString()
    }
}
