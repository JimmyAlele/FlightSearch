package com.alele.flightsearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alele.flightsearch.ui.FlightSearchViewModel

/**
 * Application home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior() // TopAppBarDefaults.pinnedScrollBehavior()
    val text by viewModel.searchText.collectAsStateWithLifecycle()
    val airportList by viewModel.airportList.collectAsStateWithLifecycle()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlightSearchTopAppBar(
                title = "Flight Search",
                scrollBehavior = scrollBehavior
            )
        },
    ) {innerPadding ->
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(innerPadding)

        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = {
                    viewModel.onSearchTextChange(text = it)
                },
                label = {Text("Search airport name")},
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { viewModel.onSearchTextChange( text = "") },
                        imageVector = Icons.Default.Close,
                        contentDescription = "close icon")
                }
            )
            Divider()

        }
    }

}

/**
 * App bar to display title
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,

) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
    )
}

@Preview(showBackground = true)
@Composable
fun FlightSearchAppPreview () {
    FlightSearchApp()
}

