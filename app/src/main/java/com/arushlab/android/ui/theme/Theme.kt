package com.arushlab.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryRed,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryRedDark,
    onPrimaryContainer = TextOnPrimary,
    secondary = PrimaryAccent,
    onSecondary = TextOnPrimary,
    secondaryContainer = DarkElevated,
    onSecondaryContainer = TextOnDark,
    tertiary = PrimaryRedLight,
    onTertiary = TextOnPrimary,
    background = DarkBackground,
    onBackground = TextOnDark,
    surface = DarkSurface,
    onSurface = TextOnDark,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextOnDark.copy(alpha = 0.7f),
    error = ErrorRed,
    onError = TextOnPrimary,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed,
    outline = TextOnDark.copy(alpha = 0.2f),
    outlineVariant = TextOnDark.copy(alpha = 0.1f),
    surfaceTint = PrimaryRed,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryRedLight.copy(alpha = 0.15f),
    onPrimaryContainer = PrimaryRedDark,
    secondary = PrimaryAccent,
    onSecondary = TextOnPrimary,
    secondaryContainer = PrimaryAccent.copy(alpha = 0.15f),
    onSecondaryContainer = PrimaryRedDark,
    tertiary = PrimaryRedLight,
    onTertiary = TextOnPrimary,
    background = BackgroundWhite,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = TextOnPrimary,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed,
    outline = Divider,
    outlineVariant = Divider,
    surfaceTint = PrimaryRed,
)

@Composable
fun ArushLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
