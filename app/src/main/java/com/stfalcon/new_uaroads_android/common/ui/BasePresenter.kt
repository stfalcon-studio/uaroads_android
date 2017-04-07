package com.stfalcon.new_cookorama_android.common.ui

import io.reactivex.disposables.CompositeDisposable

/*
 * Created by Anton Bevza on 3/28/17.
 */

abstract class BasePresenter {

    protected val disposables = CompositeDisposable()

    fun destroy() {
        disposables.clear()
    }

}