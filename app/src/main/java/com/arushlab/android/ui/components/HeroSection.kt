package com.arushlab.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arushlab.android.ui.theme.Dimens
import com.arushlab.android.ui.theme.PrimaryRed
import com.arushlab.android.ui.theme.PrimaryRedLight

@Composable
fun HeroSection(onBookTestClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryRedLight.copy(alpha = 0.2f),
                        Color.White
                    )
                )
            )
            .padding(vertical = Dimens.SpaceExtraLarge, horizontal = Dimens.SpaceMedium),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "World-Class Diagnostics at Your Doorstep",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
            Text(
                text = "Accuracy • 24/7 Support • Fast Reports • Home Collection",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
            Button(
                onClick = onBookTestClick,
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Book a Test",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
                shadowElevation = Dimens.ElevationMedium,
                modifier = Modifier.padding(top = Dimens.SpaceSmall)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = Dimens.SpaceMedium, vertical = Dimens.SpaceSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ Certified Laboratory",
                        style = MaterialTheme.typography.labelLarge.copy(color = PrimaryRed, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
