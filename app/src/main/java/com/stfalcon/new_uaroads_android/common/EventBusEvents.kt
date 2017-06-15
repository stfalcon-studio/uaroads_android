package com.stfalcon.new_uaroads_android.common

/*
 * Created by Anton Bevza on 6/15/17.
 */
data class RevealAnimationEvent(val centerX: Int,
                                val centerY: Int,
                                val color: Int,
                                val onAnimationEnd: () -> Unit)