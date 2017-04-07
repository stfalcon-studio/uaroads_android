package com.stfalcon.new_uaroads_android.features.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.stfalcon.new_cookorama_android.common.ui.BaseActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.features.main.adapter.MainScreenPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class MainActivity : BaseActivity(), MainScreenContract.View {
    override fun getLayoutResId() = R.layout.activity_main

    @Inject lateinit var presenter: MainScreenPresenter
    @Inject lateinit var pagerAdapter: MainScreenPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initPager()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initPager() {
        tabLayout.setupWithViewPager(pager)
        pager.adapter = pagerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_donate -> presenter.onDonateClicked()
            R.id.action_setting -> presenter.onSettingsClicked()
            R.id.action_list -> presenter.onTracksClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateToTracks() {
        toast("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToDonate() {
        toast("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToSettings() {
        toast("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
