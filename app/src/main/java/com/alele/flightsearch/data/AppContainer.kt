package com.alele.flightsearch.data

import android.content.Context

/**
 * App container for Dependency injection.
 */

interface AppContainer {
    val airportRepository: AirportRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineAirportRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AirportRepository]
     */
    override val airportRepository: AirportRepository by lazy {
        OfflineAirportRepository(FlightDatabase.getDatabase(context).airportDao())
    }
}