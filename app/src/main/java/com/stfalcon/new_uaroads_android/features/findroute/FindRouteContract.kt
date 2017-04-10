package com.stfalcon.new_uaroads_android.features.findroute

/*
 * Created by Anton Bevza on 4/7/17.
 */

class FindRouteContract {
    interface View {
        fun showFromAddress(address: String)
        fun showLocationChooser(key : Int)
    }

    interface Presenter {
        fun onMyLocationClicked()
        fun onFromClicked()
        fun onToClicked()
    }
}
