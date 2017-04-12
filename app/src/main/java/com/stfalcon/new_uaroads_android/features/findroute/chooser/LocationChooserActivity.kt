package com.stfalcon.new_uaroads_android.features.findroute.chooser

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import com.stfalcon.new_cookorama_android.common.ui.BaseActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.common.network.models.response.LatLng
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import com.stfalcon.new_uaroads_android.features.findroute.chooser.adapter.PlacesListAdapter
import kotlinx.android.synthetic.main.activity_location_chooser.*
import kotlinx.android.synthetic.main.item_place.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationChooserActivity : BaseActivity(), LocationChooserContract.View {

    override fun getLayoutResId() = R.layout.activity_location_chooser

    companion object {
        val RESULT_PLACE = 1
        val RESULT_MY_LOCATION = 2
        val KEY_PLACE_TITLE = "placeTitle"
        val KEY_PLACE_LOCATION = "placeLocation"

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
        presenter.onViewCreated()
    }

    private fun initInputViews() {
        tvSearch.textChanges()
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter { !it.isEmpty() }
                .subscribe {
                    presenter.onQueryChanged(getString(R.string.query_ukraine, it.toString()))
                }
        viewMyLocation.setOnClickListener {
            presenter.onMyLocationSelected()
        }
    }

    private fun initRecyclerView() {
        placeListAdapter.onClickItem = { _, item ->
            presenter.onPlaceSelected(item)
        }
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
        recyclerView.showView()
        viewMyLocation.hideView()
        placeListAdapter.clearItems()
        placeListAdapter.addItems(list)
    }

    override fun showEmptyState(location: Location?) {
        placeListAdapter.clearItems()
        location?.apply {
            recyclerView.hideView()
            viewMyLocation.showView()
            tvPlaceTitle.text = getString(R.string.place_my_location)
            tvPlaceSubtitle.text = getString(R.string.place_my_location_pos, this.longitude, this.longitude)
        }
    }

    override fun showError(error: String) {
        toast(error)
    }

    override fun returnPlaceResult(placeTitle: String, placeLocation: LatLng) {
        intent.putExtra(KEY_PLACE_TITLE, placeTitle)
        intent.putExtra(KEY_PLACE_LOCATION, placeLocation)
        setResult(RESULT_PLACE, intent)
        finish()
    }

    override fun returnMyLocationResult() {
        setResult(RESULT_MY_LOCATION, intent)
        finish()
    }
}
