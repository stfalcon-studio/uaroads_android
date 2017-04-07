package com.stfalcon.new_uaroads_android.features.findroute

import android.support.v4.app.Fragment
import com.stfalcon.new_cookorama_android.common.ui.BaseFragment
import com.stfalcon.new_uaroads_android.R
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
class FindRouteFragment : BaseFragment(), FindRouteContract.View {
    override fun getLayoutResId() = R.layout.fragment_find_route

    @Inject lateinit var presenter: FindRoutePresenter

    companion object {
        fun newInstance(): Fragment {
            return FindRouteFragment()
        }
    }

}