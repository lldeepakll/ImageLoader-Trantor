package com.application.testproject

import android.app.Application
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig


class TestApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)
    }
}