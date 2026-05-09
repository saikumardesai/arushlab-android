package com.arushlab.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arushlab.android.model.BookingModel
import com.arushlab.android.ui.theme.Dimens
import com.arushlab.android.ui.theme.PrimaryRed
import com.arushlab.android.viewmodel.TrackingViewModel
import com.arushlab.android.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    trackingId: String?,
    onBackClick: () -> Unit,
    viewModel: TrackingViewModel = hiltViewModel()
) {
    var searchInput by remember { mutableStateOf(trackingId ?: "") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(trackingId) {
        if (!trackingId.isNullOrEmpty()) {
            viewModel.fetchBooking(trackingId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.SpaceMedium)
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
            OutlinedTextField(
                value = searchInput,
                onValueChange = { searchInput = it },
                label = { Text("Enter Tracking ID (e.g. ARUSH-1024)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { if (searchInput.isNotEmpty()) viewModel.fetchBooking(searchInput) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryRed)
                    }
                },
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
            )
            
            Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

            when (val state = uiState) {
                is UiState.Loading -> {
                    if (searchInput.isNotEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryRed)
                        }
                    } else {
                        EmptyTrackingState()
                    }
                }
                is UiState.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(Dimens.SpaceMedium)
                        )
                    }
                }
                is UiState.Success -> {
                    TrackingDetailsView(booking = state.data)
                }
            }
        }
    }
}

@Composable
fun EmptyTrackingState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Gray.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        Text(
            text = "Enter your tracking ID to see live updates on your diagnostic test and download reports.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun TrackingDetailsView(booking: BookingModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Dimens.ElevationSmall),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(Dimens.SpaceMedium)) {
            Text(text = "Patient: ${booking.patientName}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(text = "Test: ${booking.testName}", style = MaterialTheme.typography.bodyLarge, color = PrimaryRed)
            Text(text = "ID: ${booking.id}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            
            Divider(modifier = Modifier.padding(vertical = Dimens.SpaceMedium))
            
            val statuses = listOf(
                "Booking Confirmed",
                "Lab Technician Assigned",
                "Sample Collected",
                "Testing in Laboratory",
                "Report Ready"
            )
            
            val currentIndex = statuses.indexOf(booking.status).coerceAtLeast(0)
            
            statuses.forEachIndexed { index, statusName ->
                TimelineStep(
                    title = statusName,
                    isCompleted = index <= currentIndex,
                    isLast = index == statuses.size - 1,
                    isActive = index == currentIndex
                )
            }
            
            if (booking.status == "Report Ready") {
                Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
                Button(
                    onClick = { /* TODO Handle Download PDF */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                    shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
                ) {
                    Text("Download Report PDF")
                }
            }
        }
    }
}

@Composable
fun TimelineStep(title: String, isCompleted: Boolean, isLast: Boolean, isActive: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) PrimaryRed else Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(if (isCompleted) PrimaryRed else Color.Gray.copy(alpha = 0.3f))
                )
            }
        }
        Spacer(modifier = Modifier.width(Dimens.SpaceMedium))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive || isCompleted) MaterialTheme.colorScheme.onSurface else Color.Gray
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
