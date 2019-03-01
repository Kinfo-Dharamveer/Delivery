package com.jointdelivery.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.jointdelivery.auth.AuthManager

@Module
class ApplicationModule(private val mContext: Context) {

    @Provides
    @Singleton
    internal fun providesSharedPreferences(): SharedPreferences {
        return getDefaultSharedPreferences(mContext)
    }

    @Provides
    @Singleton
    internal fun provideAuthManager(preferences: SharedPreferences): AuthManager {
        return AuthManager(preferences)
    }
}
