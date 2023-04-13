package com.udacity.shoestore

import android.app.Application
import timber.log.Timber

/**
 * @author Komi Donon
 * @since 4/10/2023
 */
class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
