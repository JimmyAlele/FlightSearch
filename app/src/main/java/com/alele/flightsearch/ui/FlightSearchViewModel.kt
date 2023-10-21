package com.alele.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alele.flightsearch.data.Airport
import com.alele.flightsearch.data.AirportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * ViewModel to update flight search list and items in the Room database.
 */
class FlightSearchViewModel (airportRepository: AirportRepository): ViewModel() {

    private val airRepo: AirportRepository = airportRepository

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun onSearchTextChange( text: String) {
        _searchText.update { text }
    }

    val airportList = searchText.map{
            searchText ->
        airRepo.getAirportByNameStream(searchText) }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = airRepo.getAirportByNameStream(searchText.value)
    )

    fun increasePassengersByOne(airport: Airport) {
        // run database operations inside a coroutine.
        val newPassengers: Int = airport.passengers.inc()
        viewModelScope.launch {
            withContext(Dispatchers.IO) { airRepo.updateAirport(airport.copy(passengers = newPassengers)) }
        }
    }

    fun reducePassengersByOne(airport: Airport) {
        // run database operations inside a coroutine.
        val newPassengers: Int = airport.passengers.dec()
        viewModelScope.launch {
            withContext(Dispatchers.IO) { airRepo.updateAirport(airport.copy(passengers = newPassengers)) }
        }
    }

    fun changePassengers (airport: Airport, it: String) {
        val newPassengers: Int = it.toIntOrNull() ?: 0
        viewModelScope.launch {
            withContext(Dispatchers.IO) { airRepo.updateAirport(airport.copy(passengers = newPassengers)) }
        }
    }
}



