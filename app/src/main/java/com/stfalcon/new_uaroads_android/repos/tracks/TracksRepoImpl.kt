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

package com.stfalcon.new_uaroads_android.repos.tracks

import android.content.Context
import android.os.Build
import com.stfalcon.new_uaroads_android.BuildConfig
import com.stfalcon.new_uaroads_android.common.network.services.TracksService
import com.stfalcon.new_uaroads_android.features.record.managers.Point
import com.stfalcon.new_uaroads_android.utils.DataUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/*
 * Created by Anton Bevza on 4/28/17.
 */
class TracksRepoImpl(val service: TracksService, val dataUtils: DataUtils,
                     val context: Context) : TracksRepo {

    override fun sendTrack(userId: String, points: List<Point>, routeId: String,
                           comment: String, date: String, autoRecord: Int): Completable {
        val file = File.createTempFile(System.currentTimeMillis().toString(), null, context.filesDir)
        val outputStream = context.openFileOutput(file.name, Context.MODE_PRIVATE)
        return Observable.fromIterable(points)
                .subscribeOn(Schedulers.io())
                .buffer(1000)
                .map { outputStream.write(dataUtils.buildString(it).toByteArray()) }
                .flatMapCompletable {
                    val fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file)
                    val userIdBody = RequestBody.create(MultipartBody.FORM, userId)
                    val routeIdBody = RequestBody.create(MultipartBody.FORM, routeId)
                    val commentBody = RequestBody.create(MultipartBody.FORM, comment)
                    val verBody = RequestBody.create(MultipartBody.FORM, BuildConfig.VERSION_NAME)
                    val autoRecordBody = RequestBody.create(MultipartBody.FORM, autoRecord.toString())
                    val partData = MultipartBody.Part.createFormData("data", "file", fileBody)
                    service.sendTrack(partData, userIdBody, commentBody, verBody, routeIdBody, autoRecordBody)
                }
    }

    override fun sendDeviceInfo(email: String, sensorInfo: String, uid: String): Completable {
        return service.sendDeviceInfo(email, Build.DEVICE + " " + Build.MODEL,
                "android", BuildConfig.VERSION_NAME, Build.VERSION.SDK_INT.toString(),
                sensorInfo, uid)
                .subscribeOn(Schedulers.io())
    }
}