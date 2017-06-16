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

package com.stfalcon.new_uaroads_android.features.record.views

import android.content.Context
import android.location.Location
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.ext.getCompatColor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.view_gps_indicator.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by alexandr on 10/06/15.
 */
class GpsIndicatorView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var location: Location? = null
        set(value) {
            field = value
            setAccuracy(value)
        }

    init {
        View.inflate(context, R.layout.view_gps_indicator, this)
        checkIsDataActual()
    }


    private fun setAccuracy(location: Location?) {
        when (location?.accuracy?.toInt() ?: 100) {
            in 0..29 -> {
                viewAccuracyLow.setBackgroundColor(context.getCompatColor(R.color.green))
                viewAccuracyMedium.setBackgroundColor(context.getCompatColor(R.color.green))
                viewAccuracyHigh.setBackgroundColor(context.getCompatColor(R.color.green))
            }
            in 30..60 -> {
                viewAccuracyLow.setBackgroundColor(context.getCompatColor(R.color.green))
                viewAccuracyMedium.setBackgroundColor(context.getCompatColor(R.color.green))
                viewAccuracyHigh.setBackgroundColor(context.getCompatColor(R.color.gray))
            }
            in 61..99 -> {
                viewAccuracyLow.setBackgroundColor(context.getCompatColor(R.color.green))
                viewAccuracyMedium.setBackgroundColor(context.getCompatColor(R.color.gray))
                viewAccuracyHigh.setBackgroundColor(context.getCompatColor(R.color.gray))
            }
            else -> {
                setBedSignal()
            }
        }
    }

    private fun setBedSignal() {
        viewAccuracyLow.setBackgroundColor(context.getCompatColor(R.color.red))
        viewAccuracyMedium.setBackgroundColor(context.getCompatColor(R.color.gray))
        viewAccuracyHigh.setBackgroundColor(context.getCompatColor(R.color.gray))
    }

    private fun checkIsDataActual() {
        Observable.interval(8000L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            location?.let {
                                if (System.currentTimeMillis() - it.time > 8000) {
                                    setBedSignal()
                                }
                            }
                        },
                        {
                            it.printStackTrace()
                        })
    }
}
