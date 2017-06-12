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

package com.stfalcon.new_uaroads_android.features.record

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.stfalcon.mvphelper.MvpFragment
import com.stfalcon.new_uaroads_android.BuildConfig
import com.stfalcon.new_uaroads_android.R
import com.stfalcon.new_uaroads_android.common.ui.BaseFragment
import com.stfalcon.new_uaroads_android.ext.hideView
import com.stfalcon.new_uaroads_android.ext.showView
import com.stfalcon.new_uaroads_android.features.record.service.RecordService
import com.stfalcon.new_uaroads_android.features.record.views.RecordButton
import com.stfalcon.new_uaroads_android.features.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_record.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.text.DecimalFormat
import javax.inject.Inject


/*
 * Created by Anton Bevza on 4/19/17.
 */
class RecordFragment : MvpFragment<RecordContract.Presenter, RecordContract.View>(),
        RecordContract.View, ServiceConnection {

    override fun getLayoutResId() = R.layout.fragment_record

    companion object {
        fun newInstance(): Fragment {
            return RecordFragment()
        }
    }

    private var isBound: Boolean = false

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        btnRecord?.onStartClick = { presenter?.onStartButtonClicked() }
        btnRecord?.onPauseClick = { presenter?.onPauseButtonClicked() }
        btnRecord?.onStopClick = { presenter?.onStopButtonClicked() }
        btnRecord?.onContinueClick = { presenter?.onContinueButtonClicked() }
        btnLogin?.setOnClickListener { presenter?.onLoginButtonClicked() }
        containerCopyright?.setOnClickListener { presenter?.onCopyrightClicked() }
        initChart()
    }

    override fun onStart() {
        super.onStart()
        isBound = activity.bindService(
                activity.intentFor<RecordService>(), this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            activity.unbindService(this)
        }
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        presenter?.onServiceConnected((service as RecordService.LocalBinder).service)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        presenter?.onServiceDisconnected()
    }

    override fun showLastSessionDistance(distance: Int) {
        tvDistanceLastSession?.text = formatDistance(distance)
    }

    override fun showProcessedDistance(distance: Int) {
        tvProcessedDistance?.text = formatDistance(distance)
    }

    override fun showCurrentSessionDistance(distance: Int) {
        tvDistanceCurrentSession?.text = formatDistance(distance)
    }

    override fun showForceValue(value: Double) {
        btnRecord.showPit(value)
        addEntry(value.toFloat())
    }

    override fun showIdleState() {
        btnRecord?.state = RecordButton.STATE_IDLE
        containerIdle?.showView()
        containerRecord?.hideView()
    }

    override fun showAuthState(isAuth: Boolean) {
        if (isAuth) {
            tvProcessedDistance?.showView()
            tvProcessedDistanceUnits?.showView()
            btnLogin?.hideView()
        } else {
            tvProcessedDistance?.hideView()
            tvProcessedDistanceUnits?.hideView()
            btnLogin?.showView()
        }
    }

    override fun showRecordState() {
        btnRecord?.state = RecordButton.STATE_RECORD
        containerIdle?.hideView()
        containerRecord?.showView()
    }

    override fun showPauseState() {
        btnRecord?.state = RecordButton.STATE_PAUSE
        containerIdle?.hideView()
        containerRecord?.showView()
    }

    override fun showGpsSignalStatus(location: Location) {
        gpsIndicator.location = location
    }

    override fun startRecordService() {
        activity?.startService(activity?.intentFor<RecordService>())
    }

    override fun showError(error: String?) {
        error?.let { context.toast(error) }
    }

    override fun navigateToSettings() {
        activity.startActivity(SettingsActivity.getIntent(activity))
    }

    override fun openHomePage() {
        activity.browse(BuildConfig.STFALCON)
    }

    private fun formatDistance(distance: Int) = DecimalFormat("#.##").format((distance / 1000f).toDouble())

    private fun initChart() {
        // description text
        chart.description.isEnabled = false

        // scaling and dragging and touch gestures
        chart.setTouchEnabled(false)
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        chart.axisLeft.axisMaximum = 3f
        chart.axisLeft.axisMinimum = 0f

        chart.setBackgroundColor(Color.TRANSPARENT)
        chart.legend.isEnabled = false
        chart.xAxis.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false

        // add empty data
        val data = LineData()
        chart.data = data
    }


    private fun addEntry(value: Float) {

        val data = chart.data

        if (data != null) {
            var set: ILineDataSet? = data.getDataSetByIndex(0)
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }

            data.addEntry(Entry(set.entryCount.toFloat(), value + 0.1f), 0)
            data.notifyDataChanged()

            chart.notifyDataSetChanged()
            chart.setVisibleXRangeMaximum(300F)  // limit the number of visible entries
            chart.moveViewToX(data.entryCount.toFloat())
        }
    }

    private fun createSet(): LineDataSet {

        val set = LineDataSet(null, "Dynamic Data")
        set.color = Color.RED
        set.setDrawValues(false)
        set.setDrawCircles(false)
        set.lineWidth = 2f
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        return set
    }

}