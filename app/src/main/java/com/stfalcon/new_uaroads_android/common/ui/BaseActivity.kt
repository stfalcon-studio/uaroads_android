package com.stfalcon.new_cookorama_android.common.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection

/*
 * Created by Anton Bevza on 3/28/17.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun getLayoutResId(): Int

}