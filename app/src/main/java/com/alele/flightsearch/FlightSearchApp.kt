package com.alele.flightsearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alele.flightsearch.data.Airport
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Airport Name", modifier = Modifier.weight(0.55f, fill = true))
                Text("IATA", modifier = Modifier.weight(0.15f, fill = true))
                Text("Passengers", modifier = Modifier.weight(0.3f, fill = true))
            }
            Divider()

            AirportList(
                airportList = airportList,
                onAddPassenger = viewModel::increasePassengersByOne,
                onRemovePassenger = {  },
                onPassengerChange = {  },
                modifier = Modifier,
            )
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

@Composable
private fun AirportList(
    airportList: List<Airport?>,
    modifier: Modifier = Modifier,
    onAddPassenger: (Airport) -> Unit,
    onRemovePassenger: (Airport) -> Unit,
    onPassengerChange: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(items = airportList) {airport ->
            if (airport != null) {
                AirportCard(
                    airport = airport,
                    onAddPassenger = onAddPassenger,
                    onRemovePassenger = onRemovePassenger,
                    onPassengerChange = onPassengerChange,
                )
            }
        }
    }
}

@Composable
private fun AirportCard(
    airport: Airport,
    onAddPassenger: (Airport) -> Unit,
    onRemovePassenger: (Airport) -> Unit,
    onPassengerChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable(onClick = { }),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.55f, fill = true)
            ){Text(airport.name)}
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.15f, fill = true)
            ){Text(airport.iataCode)}
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.3f, fill = true)
            ){Text(airport.passengers.toString())}
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier.weight(0.5f, fill = true))
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_circle_24 ),
                contentDescription = "Remove plane",
                modifier = modifier.clickable{}
            )
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
            TextField(
                value = airport.passengers.toString(),
                onValueChange = {

                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier.weight(1f, fill = true)
            )
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
            Icon(imageVector = Icons.Default.AddCircle,
                contentDescription = "Add item to cart",
                modifier = modifier.clickable { onAddPassenger(airport) }
            )
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
            Button(
                enabled = false,
                onClick = {

                },
                colors = if (true){
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ){
                Text ("Reset")
            }
            Spacer(modifier.weight(0.5f, fill = true))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightSearchAppPreview () {
    FlightSearchApp()
}


