package com.stfalcon.new_cookorama_android.common.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection

/*
 * Created by Anton Bevza on 3/28/17.
 */
abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getLayoutResId(), null)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun getLayoutResId(): Int

}