package com.rohegde7.videocompression.application

import android.app.Application
import android.content.Context

class VideoCompressionApplication : Application() {

    companion object {

        lateinit var mAppContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        mAppContext = this
    }
}