package com.stfalcon.new_uaroads_android.features.main

import com.stfalcon.new_cookorama_android.common.ui.BasePresenter
import com.stfalcon.new_cookorama_android.injection.scope.PerActivity
import javax.inject.Inject

/*
 * Created by Anton Bevza on 4/7/17.
 */
@PerActivity
class MainScreenPresenter @Inject constructor(val view: MainScreenContract.View) : BasePresenter(),
        MainScreenContract.Presenter {

    fun showToast() {
        view.showToast()
    }

}
