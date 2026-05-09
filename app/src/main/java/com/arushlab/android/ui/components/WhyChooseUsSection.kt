package com.arushlab.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arushlab.android.ui.theme.Dimens
import com.arushlab.android.ui.theme.PrimaryRed

@Composable
fun WhyChooseUsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpaceMedium)
    ) {
        Text(
            text = "Why Choose Us",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)) {
            TrustPillarItem(Modifier.weight(1f), "Accuracy", "NABL Standards", Icons.Default.CheckCircle)
            TrustPillarItem(Modifier.weight(1f), "Speed", "Fast Turnaround", Icons.Default.Star)
        }
        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)) {
            TrustPillarItem(Modifier.weight(1f), "Home Collection", "Expert Technicians", Icons.Default.Home)
            TrustPillarItem(Modifier.weight(1f), "Privacy", "Secure Handling", Icons.Default.Face)
        }
    }
}

@Composable
fun TrustPillarItem(modifier: Modifier = Modifier, title: String, subtitle: String, icon: ImageVector) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        elevation = CardDefaults.cardElevation(Dimens.ElevationSmall),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.SpaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryRed,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
