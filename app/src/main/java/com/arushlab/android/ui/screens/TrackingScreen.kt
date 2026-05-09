package com.arushlab.android.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arushlab.android.model.BookingModel
import com.arushlab.android.ui.theme.*
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // ─── Header ──────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Track Report",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp)
                )
                Text(
                    "Enter your tracking ID to view live status",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 20.dp, bottom = 16.dp)
                )
            }
            if (searchInput.isNotEmpty() && uiState !is UiState.Loading) {
                IconButton(
                    onClick = { viewModel.fetchBooking(searchInput) },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PrimaryRed.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = PrimaryRed)
                }
            }
        }

        // ─── Search Card ─────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(Dimens.ElevationSmall)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    label = { Text("Tracking ID") },
                    placeholder = { Text("e.g. ARUSH-1024") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryRed,
                        focusedLabelColor = PrimaryRed
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { if (searchInput.isNotEmpty()) viewModel.fetchBooking(searchInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeight),
                    enabled = searchInput.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryRed,
                        disabledContainerColor = PrimaryRed.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Track Now", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Results ─────────────────────────────
        when (val state = uiState) {
            is UiState.Loading -> {
                if (searchInput.isNotEmpty() && trackingId != null) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryRed)
                    }
                } else {
                    EmptyTrackingView()
                }
            }
            is UiState.Error -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = ErrorRedLight),
                    shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚠️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Not Found", fontWeight = FontWeight.Bold, color = ErrorRed)
                            Text(state.message, style = MaterialTheme.typography.bodySmall, color = ErrorRed.copy(alpha = 0.8f))
                        }
                    }
                }
            }
            is UiState.Success -> {
                PremiumTrackingDetails(booking = state.data)
            }
        }
    }
}

@Composable
private fun EmptyTrackingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 40.dp, end = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📋", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Track Your Test",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Enter your booking tracking ID above to see real-time status updates and download your report.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PremiumTrackingDetails(booking: BookingModel) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        // Patient Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
            elevation = CardDefaults.cardElevation(Dimens.ElevationSmall),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PrimaryRed.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧑‍⚕️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(booking.patientName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("ID: ${booking.id}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Divider)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Test", style = MaterialTheme.typography.labelSmall, color = TextHint)
                        Text(booking.testName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Date", style = MaterialTheme.typography.labelSmall, color = TextHint)
                        Text(booking.date.take(10), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Timeline
        Text("Status Timeline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        val statuses = listOf(
            StatusStep("Booking Confirmed", "Your test has been booked", "✅"),
            StatusStep("Lab Technician Assigned", "Technician will visit you", "👨‍⚕️"),
            StatusStep("Sample Collected", "Sample collected at home", "🩸"),
            StatusStep("Testing in Laboratory", "Analysis in progress", "🔬"),
            StatusStep("Report Ready", "Download your report", "📄")
        )

        val currentIndex = statuses.indexOfFirst { it.title == booking.status }.coerceAtLeast(0)

        statuses.forEachIndexed { index, step ->
            PremiumTimelineStep(
                step = step,
                isCompleted = index <= currentIndex,
                isActive = index == currentIndex,
                isLast = index == statuses.size - 1
            )
        }

        if (booking.status == "Report Ready") {
            Spacer(modifier = Modifier.height(20.dp))
            val context = androidx.compose.ui.platform.LocalContext.current
            Button(
                onClick = { 
                    if (!booking.reportUrl.isNullOrEmpty()) {
                        try {
                            android.widget.Toast.makeText(context, "Downloading Report...", android.widget.Toast.LENGTH_SHORT).show()
                            val url = if (booking.reportUrl.startsWith("http")) {
                                booking.reportUrl
                            } else {
                                com.arushlab.android.BuildConfig.SUPABASE_URL + "/storage/v1/object/public/reports/" + booking.reportUrl
                            }
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                            intent.data = android.net.Uri.parse(url)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.widget.Toast.makeText(context, "Unable to open report link", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        android.widget.Toast.makeText(context, "Report file not found. Please contact support.", android.widget.Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ButtonHeight),
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download Report PDF", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Note: Report will be available for download only for 2 days.",
                style = MaterialTheme.typography.labelMedium,
                color = ErrorRed,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class StatusStep(val title: String, val desc: String, val emoji: String)

@Composable
private fun PremiumTimelineStep(step: StatusStep, isCompleted: Boolean, isActive: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline line + dot
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) PrimaryRed
                        else if (isActive) PrimaryRed.copy(alpha = 0.3f)
                        else SurfaceGray
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                } else {
                    Text(step.emoji, fontSize = 14.sp)
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(48.dp)
                        .background(
                            if (isCompleted) PrimaryRed.copy(alpha = 0.5f)
                            else SurfaceGray
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(
                step.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive || isCompleted) MaterialTheme.colorScheme.onSurface else TextHint
            )
            Text(
                step.desc,
                style = MaterialTheme.typography.bodySmall,
                color = if (isActive) TextSecondary else TextHint
            )
            if (!isLast) Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
