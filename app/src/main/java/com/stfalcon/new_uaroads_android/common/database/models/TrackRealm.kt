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

package com.stfalcon.new_uaroads_android.common.database.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
 * Created by Anton Bevza on 4/25/17.
 */

open class TrackRealm : RealmObject() {

    companion object {
        val STATUS_NOT_SENT = 0
        val STATUS_SENT = 1
        val STATUS_IS_RECODING = 2
        val STATUS_IS_SENDING = 3
    }

    @PrimaryKey
    open var id: String = ""
    open var distance = 0
    open var timestamp: Long = 0
    open var comment: String? = null
    open var status = STATUS_IS_RECODING
    open var points: RealmList<PointRealm> = RealmList()
    open var autoRecord: Int = 0

}
