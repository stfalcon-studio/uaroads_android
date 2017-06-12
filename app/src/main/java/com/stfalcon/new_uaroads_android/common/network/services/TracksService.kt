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

package com.stfalcon.new_uaroads_android.common.network.services

import io.reactivex.Completable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/*
 * Created by Anton Bevza on 4/28/17.
 */
interface TracksService {
    @Multipart
    @POST("add")
    fun sendTrack(
            @Part file: MultipartBody.Part,
            @Part("uid") uid: RequestBody,
            @Part("comment") comment: RequestBody,
            @Part("app_ver") appVersion: RequestBody,
            @Part("routeId") routeId: RequestBody,
            @Part("auto_record") autoRecord: RequestBody
    ): Completable

    @FormUrlEncoded
    @POST("http://uaroads.com/register-device")
    fun sendDeviceInfo(
            @Field("email") email: String,
            @Field("device_name") deviceName: String,
            @Field("os") os: String,
            @Field("app_ver") appVersion: String,
            @Field("os_version") osVersion: String,
            @Field("sensor_info") sensorInfo: String,
            @Field("uid") uid: String
    ): Completable
}