package com.alele.flightsearch

import android.app.Application
import com.alele.flightsearch.data.AppContainer
import com.alele.flightsearch.data.AppDataContainer

class FlightSearchApplication: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}