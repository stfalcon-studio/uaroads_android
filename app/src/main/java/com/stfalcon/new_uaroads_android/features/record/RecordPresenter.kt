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

import com.stfalcon.mvphelper.Presenter
import com.stfalcon.new_uaroads_android.common.analytics.AnalyticsManager
import com.stfalcon.new_uaroads_android.common.data.preferences.Settings
import com.stfalcon.new_uaroads_android.common.database.RealmService
import com.stfalcon.new_uaroads_android.common.injection.InjectionConsts
import com.stfalcon.new_uaroads_android.features.record.service.IRecordService
import com.stfalcon.new_uaroads_android.features.record.service.RecordService
import com.stfalcon.new_uaroads_android.repos.user.UserRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by Anton Bevza on 4/19/17.
 */
class RecordPresenter @Inject constructor(val realmService: RealmService,
                                          val analyticsManager: AnalyticsManager,
                                          val settings: Settings,
                                          val userRepo: UserRepo,
                                          @Named(InjectionConsts.NAME_USER_ID) val userId: String) :
        Presenter<RecordContract.View>(),
        RecordContract.Presenter {
    private var recordService: IRecordService? = null
    private var forceValuesSubscription: Disposable? = null
    private var trackDistanceSubscription: Disposable? = null
    private var pureLocationSubscription: Disposable? = null

    override fun onServiceConnected(service: IRecordService) {
        recordService = service

        recordService?.let { service ->
            disposables.add(service.getStateFlow()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        when (it) {
                            RecordService.STATE_RECORD -> {
                                view?.showRecordState()
                                subscribeOnService(service)
                            }
                            RecordService.STATE_PAUSE -> {
                                view?.showPauseState()
                                disposeServiceSubscriptions()
                            }
                            RecordService.STATE_IDLE -> {
                                view?.showIdleState()
                                view?.showAuthState(settings.isAuthorized())
                                updateUserStatistics()
                                disposeServiceSubscriptions()
                            }
                        }

                    }
            )
        }
    }

    override fun onServiceDisconnected() {
        recordService = null
    }

    override fun onStartButtonClicked() {
        recordService?.startRecord()
        view?.startRecordService()
        analyticsManager.sendStartManualRecord()
    }

    override fun onPauseButtonClicked() {
        recordService?.pauseRecord()
        analyticsManager.sendPauseManualRecord()
    }

    override fun onContinueButtonClicked() {
        recordService?.continueRecord()
    }

    override fun onStopButtonClicked() {
        recordService?.stopRecord()
        analyticsManager.sendStopManualRecord()
    }

    override fun onViewAttached(view: RecordContract.View, created: Boolean) {
        super.onViewAttached(view, created)
        view.showIdleState()
        view.showForceValue(0.0)
        view.showCurrentSessionDistance(0)
        updateUserStatistics()
        view.showAuthState(settings.isAuthorized())
    }

    override fun onViewDetached() {
        super.onViewDetached()
        disposables.clear()
    }

    override fun onDestroy() {
        realmService.closeRealm()
    }

    override fun onLoginButtonClicked() {
        view?.navigateToSettings()
    }

    override fun onCopyrightClicked() {
        view?.openHomePage()
    }

    private fun updateUserStatistics() {
        if (settings.isAuthorized()) {
            userRepo.getUserStatistics(userId, settings.getUserEmail())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                it?.let {
                                    view?.showProcessedDistance((it.totalDistance * 1000).toInt())

                                    if (!it.status) {
                                        it.error?.let {
                                            view?.showError(it)
                                        }
                                    }
                                }
                            },
                            {
                                it?.message?.let { view?.showError(it) }
                            }
                    )
        }
        view?.showLastSessionDistance(realmService.getLastTrackDistance())
    }

    private fun subscribeOnService(service: IRecordService): Boolean {
        forceValuesSubscription = service.getForceValuesFlow()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.showForceValue(it) }, Throwable::printStackTrace)
        disposables.add(forceValuesSubscription)

        trackDistanceSubscription = service.getTrackDistanceFlow()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.showCurrentSessionDistance(it) }, Throwable::printStackTrace)
        disposables.add(trackDistanceSubscription)

        pureLocationSubscription = service.getPureLocationFlow()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.showGpsSignalStatus(it) }, Throwable::printStackTrace)
        return disposables.add(pureLocationSubscription)
    }

    private fun disposeServiceSubscriptions() {
        forceValuesSubscription?.dispose()
        trackDistanceSubscription?.dispose()
        pureLocationSubscription?.dispose()
    }
}