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

package com.stfalcon.new_uaroads_android.features.bestroute

/*
 * Created by Anton Bevza on 4/7/17.
 */

import android.content.Context
import com.stfalcon.mvphelper.PresenterLoader
import com.stfalcon.new_uaroads_android.common.injection.InjectionConsts
import com.stfalcon.new_uaroads_android.common.network.services.RoutesService
import com.stfalcon.new_uaroads_android.repos.routes.RoutesRepo
import com.stfalcon.new_uaroads_android.repos.routes.UaroadsRoutesRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class BestRouteModule {

    @Provides
    fun providePresenterLoader(context: Context, presenter: BestRoutePresenter)
            : PresenterLoader<BestRouteContract.Presenter> = PresenterLoader(context, presenter)

    @Provides fun provideRepo(service: RoutesService): RoutesRepo = UaroadsRoutesRepo(service)

    @Provides fun provideRouteInfoFormatter(context: Context) = RouteInfoFormatter(context)

    @Named(InjectionConsts.NAME_LOCATION_FROM)
    @Provides fun provideLocationFrom(activity: BestRouteActivity) = activity.locationFrom

    @Named(InjectionConsts.NAME_LOCATION_TO)
    @Provides fun provideLocationTo(activity: BestRouteActivity) = activity.locationTo

    @Named(InjectionConsts.NAME_TITLE_FROM)
    @Provides fun provideTitleFrom(activity: BestRouteActivity) = activity.titleFrom

    @Named(InjectionConsts.NAME_TITLE_TO)
    @Provides fun provideTitleTo(activity: BestRouteActivity) = activity.titleTo

}
