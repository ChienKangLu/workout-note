package com.chienkanglu.workoutnote.log

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber

@SuppressLint("LogNotTimber")
class ReleaseTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        when (priority) {
            Log.INFO, Log.WARN -> {
                Log.println(priority, tag, message)
            }

            Log.ERROR -> {
                Log.e(tag, message, t)
            }
        }
    }
}
