package com.stfalcon.new_cookorama_android.injection.module

/*
 * Created by Anton Bevza on 4/7/17.
 */

import android.support.v4.app.FragmentManager
import com.stfalcon.new_uaroads_android.features.main.MainActivity
import com.stfalcon.new_uaroads_android.features.main.MainScreenContract
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule {

    @Provides
    fun providesFragmentManager(activity: MainActivity): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides fun provideView(mainActivity: MainActivity): MainScreenContract.View {
        return mainActivity
    }

}
