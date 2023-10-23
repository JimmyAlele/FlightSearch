package com.alele.flightsearch.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alele.flightsearch.data.Airport
import com.alele.flightsearch.data.AirportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * ViewModel to update flight search list and items in the Room database.
 */
class FlightSearchViewModel (airportRepository: AirportRepository): ViewModel() {

    /**
     * Using MutableSharedFlow because MutableStateFlow does not emit the same value consecutively
     * MutableSharedFlow does not take an initial value
     */
    private val _newPass = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val newPass = _newPass.asSharedFlow()

    init{
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                newPass.collect{
                }
            }
        }

        // providing initial value to be available to combine transformation
        // combine needs values from all the flows in order to trigger transformation
        viewModelScope.launch {
            withContext(Dispatchers.IO) {_newPass.emit(0)}
        }
    }

    private val airRepo: AirportRepository = airportRepository

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    /**
     * Using MutableSharedFlow because MutableStateFlow does not emit the same value consecutively
     * MutableSharedFlow does not take an initial value
     */

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun onSearchTextChange( text: String) {
        _searchText.update { text }
        Log.d("called", "_searchText1: ${_searchText.value}")
    }

    val airportList = newPass.combine(_searchText){ _, text ->
        airRepo.getAirportByNameStream(text).first()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = null
    )

//    Another option would be to get the full airports list and use transform operations to filter
//    it as desired.
//    Avoiding loading the full airport list in memory and instead relying on room db queries to do the work

    val airports = airRepo.getAllAirportStream()
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = null
    )

    fun increasePassengersByOne(airport: Airport) {
        // run database operations inside a coroutine.
        val newPassengers: Int = airport.passengers.inc()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                airRepo.updateAirport(airport.copy(passengers = newPassengers))
                _newPass.emit(airport.passengers)
            }
        }
    }

    fun reducePassengersByOne(airport: Airport) {
        // run database operations inside a coroutine.
        val newPassengers: Int = airport.passengers.dec()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                airRepo.updateAirport(airport.copy(passengers = newPassengers))
                _newPass.emit(airport.passengers)
            }
        }
    }

    fun changePassengers (airport: Airport, it: String) {
        val newPassengers: Int = it.toIntOrNull() ?: 0
        viewModelScope.launch {
            withContext(Dispatchers.IO) { airRepo.updateAirport(airport.copy(passengers = newPassengers)) }
            _newPass.emit(airport.passengers)
        }
    }
}



