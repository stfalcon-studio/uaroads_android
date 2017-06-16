/*
 * Copyright (c) 2017 stfalcon.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.stfalcon.new_uaroads_android.features.places

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.widget.textChanges
import com.stfalcon.mvphelper.MvpActivity
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.network.models.LatLng
import com.stfalcon.new_uaroads_android.common.network.models.response.GeoPlacePrediction
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import com.stfalcon.new_uaroads_android.features.places.adapter.PlacesListAdapter
import kotlinx.android.synthetic.main.activity_location_chooser.*
import kotlinx.android.synthetic.main.item_place.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationChooserActivity : MvpActivity<LocationChooserContract.Presenter, LocationChooserContract.View>(),
        LocationChooserContract.View {

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

    @Inject lateinit var placeListAdapter: PlacesListAdapter
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initInputViews()
        initRecyclerView()
    }

    private fun initInputViews() {
        etSearch.textChanges()
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter { !it.isEmpty() }
                .subscribe {
                    presenter?.onQueryChanged(getString(R.string.query_ukraine, it.toString()))
                }
        etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    && v.text.isNotEmpty()) {
                presenter?.onActionSearchClicked(getString(R.string.query_ukraine, v.text), v.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        viewMyLocation.setOnClickListener {
            presenter?.onMyLocationSelected()
        }
    }

    private fun initRecyclerView() {
        placeListAdapter.onClickItem = { _, item ->
            presenter?.onPlaceSelected(item)
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
        toolbar.setNavigationOnClickListener { presenter?.onClickNavigateButton() }
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
        location?.let {
            recyclerView.hideView()
            viewMyLocation.showView()
            tvPlaceTitle.text = getString(R.string.place_my_location)
            tvPlaceSubtitle.text = getString(R.string.place_my_location_pos, it.longitude, it.longitude)
        }
    }

    override fun showNoResutError(query: String) {
        toast(getString(R.string.search_no_result_error, query))
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
