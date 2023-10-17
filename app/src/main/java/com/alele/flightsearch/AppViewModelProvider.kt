package com.alele.flightsearch

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alele.flightsearch.ui.FlightSearchViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Flight Search app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for FlightSearchViewModel
        initializer {
            FlightSearchViewModel(flightSearchApplication().container.airportRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlightSearchApplication].
 */
fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)