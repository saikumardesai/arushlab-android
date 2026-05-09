package com.arushlab.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arushlab.android.ui.theme.Dimens
import com.arushlab.android.ui.theme.PrimaryRed

@Composable
fun ContactSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SpaceMedium)
    ) {
        Text(
            text = "Contact & Location",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(Dimens.ElevationSmall),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(Dimens.SpaceMedium)) {
                ContactItem(icon = Icons.Default.LocationOn, text = "Beside Sangmeshwar Hospital,\nCanara Bank to KEB Road,\nBidar - 585403")
                Divider(modifier = Modifier.padding(vertical = Dimens.SpaceSmall))
                ContactItem(icon = Icons.Default.Phone, text = "+91 9482724054\n+91 7483554790")
                Divider(modifier = Modifier.padding(vertical = Dimens.SpaceSmall))
                ContactItem(icon = Icons.Default.Email, text = "support@arushlab.com")
                
                Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
                Button(
                    onClick = { /* TODO Support Action */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                ) {
                    Text("Contact Support via WhatsApp")
                }
            }
        }
    }
}

@Composable
fun ContactItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(Dimens.SpaceMedium))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
        )
    }
}
