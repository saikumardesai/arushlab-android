package com.arushlab.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arushlab.android.ui.theme.Dimens
import com.arushlab.android.ui.theme.PrimaryRed

@Composable
fun SpecializedServicesSection() {
    val services = listOf(
        "Blood Tests", "Hormone Analysis", "Master Health Checkups",
        "Diabetes Tests", "Thyroid Tests", "Vitamin Tests"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Specialized Services",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = Dimens.SpaceMedium)
        )
        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.SpaceMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
        ) {
            items(services) { service ->
                ServiceCardItem(title = service)
            }
        }
    }
}

@Composable
fun ServiceCardItem(title: String) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = PrimaryRed.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(Dimens.SpaceSmall),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryRed
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
