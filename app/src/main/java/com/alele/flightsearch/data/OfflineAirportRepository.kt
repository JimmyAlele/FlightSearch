package com.alele.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportRepository(private val airportDao: AirportDao): AirportRepository {

    override fun getAllAirportStream(): Flow<List<Airport>?> = airportDao.getAllAirport()
    override fun getAirportByNameStream(name: String): Flow<List<Airport>?> = airportDao.getAirportByName(name)

    override fun getAirportStream(id: Int): Flow<Airport?> = airportDao.getAirport(id)

    override suspend fun insertAirport(airport: Airport) = airportDao.insert(airport)

    override suspend fun deleteAirport(airport: Airport) = airportDao.delete(airport)

    override suspend fun updateAirport(airport: Airport) = airportDao.update(airport)
}