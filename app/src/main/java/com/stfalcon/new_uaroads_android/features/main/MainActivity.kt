package com.stfalcon.new_uaroads_android.features.main

import android.os.Bundle
import com.stfalcon.new_cookorama_android.common.ui.BaseActivity
import com.stfalcon.new_uaroads_android.R
import org.jetbrains.anko.toast
import javax.inject.Inject

class MainActivity : BaseActivity(), MainScreenContract.View {
    override fun showToast() {
        toast("Uiiiiii!")
    }

    override fun getLayoutResId() = R.layout.activity_main

    @Inject lateinit var presenter: MainScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.showToast()

    }
}
