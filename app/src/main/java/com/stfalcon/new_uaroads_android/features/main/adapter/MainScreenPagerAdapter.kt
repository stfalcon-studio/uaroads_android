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

package com.stfalcon.new_uaroads_android.features.main.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.features.findroute.FindRouteFragment
import com.stfalcon.new_uaroads_android.features.record.RecordFragment
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
class MainScreenPagerAdapter @Inject constructor(val context: Context, fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FindRouteFragment.newInstance()
            1 -> RecordFragment.newInstance()
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