package com.stfalcon.new_uaroads_android.features.findroute

import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.stfalcon.new_cookorama_android.common.ui.BaseFragment
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.features.findroute.chooser.LocationChooserActivity
import kotlinx.android.synthetic.main.fragment_find_route.*
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
class FindRouteFragment : BaseFragment(), FindRouteContract.View {

    override fun getLayoutResId() = R.layout.fragment_find_route

    @Inject lateinit var presenter: FindRoutePresenter

    companion object {
        val KEY_GET_TO_LOCATION = 1
        val KEY_GET_FROM_LOCATION = 2

        fun newInstance(): Fragment {
            return FindRouteFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMyLocation.setOnClickListener { presenter.onMyLocationClicked() }
        tvFrom.setOnClickListener { presenter.onFromClicked() }
        tvTo.setOnClickListener { presenter.onToClicked() }
    }

    override fun showFromAddress(address: String) {
        tvFrom.text = address
    }

    override fun showLocationChooser(key: Int, location: Location?) {
        startActivityForResult(LocationChooserActivity.getIntent(activity, location), key)
    }

}