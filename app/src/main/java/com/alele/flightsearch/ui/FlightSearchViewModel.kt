package com.alele.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alele.flightsearch.data.Airport
import com.alele.flightsearch.data.AirportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * ViewModel to update flight search list and items in the Room database.
 */
class FlightSearchViewModel (airportRepository: AirportRepository): ViewModel() {

    val airRepo: AirportRepository = airportRepository

    private var _airportList = MutableStateFlow(listOf<Airport?>())
    var airportList = _airportList.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange( text: String) {
        _searchText.update { text }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _airportList.update{airRepo.getAirportByNameStream(text).first()}
                //airportList = _airportList.asStateFlow()
            }
        }
    }

}




