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

package com.stfalcon.new_uaroads_android.common.injection.modules

import com.stfalcon.new_uaroads_android.common.database.RealmService
import dagger.Module
import dagger.Provides
import io.realm.Realm

/*
 * Created by Anton Bevza on 4/26/17.
 */
@Module
class RealmModule {
    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()

    @Provides
    fun provideRealmService(realm: Realm): RealmService = RealmService(realm)
}