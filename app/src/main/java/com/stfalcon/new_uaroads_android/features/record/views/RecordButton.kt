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
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import kotlinx.android.synthetic.main.view_record_button.view.*

/**
 * Created by alexandr on 08/06/15.
 */
class RecordButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    companion object {
        val STATE_RECORD = 1
        val STATE_PAUSE = 2
        val STATE_IDLE = 3
    }

    var onStartClick: (() -> Unit)? = null
    var onStopClick: (() -> Unit)? = null
    var onPauseClick: (() -> Unit)? = null
    var onContinueClick: (() -> Unit)? = null

    var state: Int = STATE_IDLE
        set(value) {
            field = value
            updateState(value)
        }

    private var lastPitTime = 0L
    private var pitValue = 0.0

    private var pulse: Animation? = null
    private var pulse2x: Animation? = null
    private var pit: Animation? = null
    private var pit2x: Animation? = null
    private var pit3x: Animation? = null

    init {
        View.inflate(getContext(), R.layout.view_record_button, this)
        containerStart.setOnClickListener(this)
        containerPause.setOnClickListener(this)
        containerStop.setOnClickListener(this)
        pulse = AnimationUtils.loadAnimation(context, R.anim.pulse)
        pulse2x = AnimationUtils.loadAnimation(context, R.anim.pulse_2x)
        pit = AnimationUtils.loadAnimation(context, R.anim.pit)
        pit2x = AnimationUtils.loadAnimation(context, R.anim.pit2x)
        pit3x = AnimationUtils.loadAnimation(context, R.anim.pit3x)
        ivStartEffect.startAnimation(pulse)
        ivPitIndicator.animation = pit
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.containerStart -> {
                onStartClick?.invoke()
            }
            R.id.containerPause -> {
                if (state == STATE_RECORD) {
                    onPauseClick?.invoke()
                } else {
                    onContinueClick?.invoke()
                }
            }
            R.id.containerStop -> {
                onStopClick?.invoke()
            }
        }
    }

    fun showPit(value: Double) {
        if (pitValue < value) pitValue = value
        if (lastPitTime < System.currentTimeMillis() - 300 && ivPitIndicator.animation == null) {
            lastPitTime = System.currentTimeMillis()
            if (pitValue > 0.3 && pitValue < 0.6) {
                ivPitIndicator.startAnimation(pit)
            } else if (pitValue > 0.6 && pitValue < 1) {
                ivPitIndicator.startAnimation(pit2x)
            } else if (pitValue > 1) {
                ivPitIndicator.startAnimation(pit3x)
            }
            pitValue = 0.0
        }
    }

    private fun updateState(state: Int) {
        when (state) {
            STATE_RECORD -> {
                containerPauseStop.showView()
                containerStart.hideView()
                tvPause.text = context.getText(R.string.record_pause)
                ivStartEffect2.startAnimation(pulse2x)
            }
            STATE_PAUSE -> {
                containerPauseStop.showView()
                containerStart.hideView()
                tvPause.text = context.getText(R.string.record_continue)
                if (ivStartEffect2.animation != null) {
                    ivStartEffect2.animation.cancel()
                }
            }
            STATE_IDLE -> {
                containerPauseStop.hideView()
                containerStart.showView()
                if (ivStartEffect2.animation != null) {
                    ivStartEffect2.animation.cancel()
                }
            }
            else -> throw TODO("Wrong record button state")
        }
    }
}
