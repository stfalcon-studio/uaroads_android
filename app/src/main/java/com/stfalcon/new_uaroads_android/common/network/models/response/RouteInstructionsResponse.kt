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

package com.stfalcon.new_uaroads_android.common.network.models.response

import com.google.gson.annotations.SerializedName

/*
 * Created by Anton Bevza on 4/14/17.
 */
data class Route(@SerializedName("route_summary") val routeSummary: RouteSummary?,
                 @SerializedName("found_alternative") val foundAlternative: Boolean,
                 @SerializedName("route_geometry") val routeGeometry: String,
                 @SerializedName("status_message") val statusMessage: String,
                 @SerializedName("status") val status: Int)

data class RouteSummary(@SerializedName("end_point") val endPoint: String,
                        @SerializedName("start_point") val startPoint: String,
                        @SerializedName("total_time") val totalTime: Long,
                        @SerializedName("total_distance") val totalDistance: Int,
                        @SerializedName("quality") val quality: Array<Int>)