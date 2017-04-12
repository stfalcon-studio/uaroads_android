package com.stfalcon.new_cookorama_android.common.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

/*
 * Created by Anton Bevza on 3/28/17.
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        parseIntent()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
    }

    open fun parseIntent(){
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun getLayoutResId(): Int

}