package com.arushlab.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arushlab.android.model.TestModel
import com.arushlab.android.ui.components.*
import com.arushlab.android.ui.theme.Dimens
import com.arushlab.android.viewmodel.BookingViewModel
import com.arushlab.android.viewmodel.HomeViewModel
import com.arushlab.android.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTracking: (String?) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    val bookingState by bookingViewModel.bookingState.collectAsState()
    var selectedTestForBooking by remember { mutableStateOf<TestModel?>(null) }
    
    LaunchedEffect(bookingState) {
        if (bookingState is UiState.Success) {
            val trackingId = (bookingState as UiState.Success).data.id
            selectedTestForBooking = null
            bookingViewModel.resetBookingState()
            onNavigateToTracking(trackingId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "ARUSH Lab & Diagnostics",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                actions = {
                    TextButton(onClick = { onNavigateToTracking(null) }) {
                        Text("Track Report", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = Dimens.SpaceLarge)
        ) {
            item {
                HeroSection(onBookTestClick = { /* Scroll to pricing or open modal */ })
            }
            
            item {
                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                SpecializedServicesSection()
            }
            
            item {
                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                WhyChooseUsSection()
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                Text(
                    text = "Diagnostic Test Prices",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = Dimens.SpaceMedium)
                )
                SearchBarComponent(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) }
                )
            }

            when (val state = uiState) {
                is UiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is UiState.Error -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                is UiState.Success -> {
                    val tests = state.data
                    if (tests.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text(text = "No tests found.")
                            }
                        }
                    } else {
                        items(tests) { test ->
                            TestPriceCard(
                                test = test,
                                onBookNowClick = { selectedTest ->
                                    selectedTestForBooking = selectedTest
                                }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                ContactSection()
            }
        }
        
        selectedTestForBooking?.let { test ->
            BookingBottomSheet(
                test = test,
                onDismiss = { selectedTestForBooking = null },
                onSubmit = { name, mobile, address ->
                    bookingViewModel.submitBooking(name, mobile, address, test)
                },
                isLoading = bookingState is UiState.Loading,
                errorMessage = if (bookingState is UiState.Error) (bookingState as UiState.Error).message else null
            )
        }
    }
}
