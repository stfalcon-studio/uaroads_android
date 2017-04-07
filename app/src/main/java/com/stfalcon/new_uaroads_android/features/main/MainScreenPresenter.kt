package com.stfalcon.new_uaroads_android.features.main

import com.stfalcon.new_cookorama_android.common.ui.BasePresenter
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
class MainScreenPresenter @Inject constructor(val view: MainScreenContract.View) : BasePresenter(),
        MainScreenContract.Presenter {

    override fun onTracksClicked() {
        view.navigateToTracks()
    }

    override fun onDonateClicked() {
        view.navigateToDonate()
    }

    override fun onSettingsClicked() {
        view.navigateToSettings()
    }


}
