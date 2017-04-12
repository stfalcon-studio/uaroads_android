package com.stfalcon.new_uaroads_android.features.findroute.chooser

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import com.stfalcon.new_cookorama_android.common.ui.BaseActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.features.findroute.chooser.adapter.PlacesListAdapter
import kotlinx.android.synthetic.main.activity_location_chooser.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationChooserActivity : BaseActivity(), LocationChooserContract.View {

    override fun getLayoutResId() = R.layout.activity_location_chooser

    companion object {
        private val KEY_LOCATION = "location"

        fun getIntent(context: Context, location: Location?) = context.intentFor<LocationChooserActivity>(
                KEY_LOCATION to location
        )
    }

    @Inject lateinit var presenter: LocationChooserPresenter
    @Inject lateinit var placeListAdapter: PlacesListAdapter
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initInputViews()
        initRecyclerView()
    }

    private fun initInputViews() {
        tvSearch.textChanges()
                .debounce(500, TimeUnit.MICROSECONDS)
                .filter { !it.isEmpty() }
                .subscribe {
                    presenter.onQueryChanged(getString(R.string.query_ukraine, it.toString()))
                }
    }

    private fun initRecyclerView() {
        recyclerView.adapter = placeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun parseIntent() {
        super.parseIntent()
        location = intent.getParcelableExtra(KEY_LOCATION)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { presenter.onClickNavigateButton() }
    }

    override fun navigateBack() {
        finish()
    }

    override fun showResults(list: List<GeoPlacePrediction>) {
        recyclerView.visibility = View.VISIBLE
        placeListAdapter.clearItems()
        placeListAdapter.addItems(list)
    }

    override fun showEmptyState(location: Location) {
        recyclerView.visibility = View.GONE
        placeListAdapter.clearItems()
    }

    override fun showError(error: String) {
        recyclerView.visibility = View.GONE
        placeListAdapter.clearItems()
        toast(error)
    }

}
