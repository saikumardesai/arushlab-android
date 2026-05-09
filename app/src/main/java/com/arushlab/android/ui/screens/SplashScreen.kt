package com.arushlab.android.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arushlab.android.ui.theme.*
import com.arushlab.android.viewmodel.AuthViewModel
import io.github.jan.supabase.gotrue.SessionStatus
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val sessionStatus by viewModel.sessionStatus.collectAsState(initial = SessionStatus.LoadingFromStorage)

    LaunchedEffect(sessionStatus) {
        if (sessionStatus != SessionStatus.LoadingFromStorage) {
            startAnimation = true
            delay(2000)
            if (sessionStatus is SessionStatus.Authenticated) {
                onNavigateToHome()
            } else {
                onNavigateToAuth()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryRed, PrimaryRedLight, PrimaryAccent)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // Lab emoji logo
            Text("🔬", fontSize = 72.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "ARUSH Lab",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = (-1).sp
                )
            )
            Text(
                text = "& Diagnostics",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.85f),
                    letterSpacing = 2.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "World-Class Diagnostics\nat Your Doorstep",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
