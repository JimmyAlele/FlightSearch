package com.alele.flightsearch.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Airport] from a given data source.
 */
interface AirportRepository {
    /**
     * Retrieve all the airports from the the given data source.
     */
    fun getAllAirportStream(): Flow<List<Airport>?>

    /**
     * Retrieve all the airports by name from the the given data source.
     */
    fun getAirportByNameStream(name: String): Flow<List<Airport>?>

    /**
     * Retrieve airport from the given data source that matches with the [id].
     */
    fun getAirportStream(id: Int): Flow<Airport?>

    /**
     * Insert airport in the data source
     */
    suspend fun insertAirport(airport: Airport)

    /**
     * Delete airport from the data source
     */
    suspend fun deleteAirport(airport: Airport)

    /**
     * Update airport in the data source
     */
    suspend fun updateAirport(airport: Airport)
}