package com.stfalcon.new_uaroads_android.features.main.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.features.findroute.FindRouteFragment
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
class MainScreenPagerAdapter @Inject constructor(val context: Context, fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FindRouteFragment.newInstance()
            1 -> FindRouteFragment.newInstance()
            else -> TODO("wrong page index")
        }
    }

    override fun getCount() = 2


    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.tab_routes)
            1 -> context.getString(R.string.tab_record)
            else -> TODO("wrong page index")
        }
    }
}