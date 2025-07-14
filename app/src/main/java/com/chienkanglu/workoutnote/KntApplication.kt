package com.chienkanglu.workoutnote

import android.app.Application
import com.chienkanglu.workoutnote.log.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class KntApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        } else {
            plant(ReleaseTree())
        }
    }
}
