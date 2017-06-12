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

package com.stfalcon.new_uaroads_android.features.intro

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.ext.getCompatDrawable
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import org.jetbrains.anko.intentFor


/*
 * Created by Anton Bevza on 4/18/17.
 */
class IntroActivity : AhoyOnboarderActivity() {

    companion object {
        fun getIntent(context: Context) = context.intentFor<IntroActivity>()
    }

    override fun onFinishButtonPressed() {
        startActivity(intentFor<MainActivity>())
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val card1 = AhoyOnboarderCard("", getString(R.string.intro_screen_1), R.drawable.slide1)
        card1.setIconLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0)
        card1.setDescriptionColor(R.color.black)

        val card2 = AhoyOnboarderCard("", getString(R.string.intro_screen_2), R.drawable.slide2)
        card2.setIconLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0)
        card2.setDescriptionColor(R.color.black)

        setOnboardPages(listOf(card1, card2))

        setFinishButtonTitle(getString(R.string.intro_start).toUpperCase())
        setFinishButtonDrawableStyle(getCompatDrawable(R.drawable.selector_button_orange))

        setColorBackground(R.color.colorPrimary)
    }


}