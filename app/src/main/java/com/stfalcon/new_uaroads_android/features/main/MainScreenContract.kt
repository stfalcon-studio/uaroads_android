package com.stfalcon.new_uaroads_android.features.main

/*
 * Created by Anton Bevza on 4/7/17.
 */

class MainScreenContract {
    interface View {
        fun navigateToTracks()
        fun navigateToDonate()
        fun navigateToSettings()
    }

    interface Presenter {
        fun onTracksClicked()
        fun onDonateClicked()
        fun onSettingsClicked()
    }
}
