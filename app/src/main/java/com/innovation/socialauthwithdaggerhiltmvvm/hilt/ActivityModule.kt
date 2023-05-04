package com.innovation.socialauthwithdaggerhiltmvvm.hilt

import android.app.Activity
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.FacebookAuth
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.GoogleAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * @author Created by sumit on 08/04/2022
 *
 */
@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    /**
     * This method used for provide Google Auth instance to Hilt DI
     *
     */
    @Provides
    fun provideGoogleBuilder(activity: Activity): GoogleAuth.Builder {
        return GoogleAuth.Builder(activity)
    }

    /**
     * This method used for provide Google Auth instance to Hilt DI
     *
     */
    @Provides
    fun provideFacebookBuilder(activity: Activity): FacebookAuth.Builder {
        return FacebookAuth.Builder(activity)
    }

}