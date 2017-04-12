package com.stfalcon.new_uaroads_android.ext

import android.view.View

/*
 * Created by Anton Bevza on 4/12/17.
 */
fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.hideView() {
    this.visibility = View.GONE
}