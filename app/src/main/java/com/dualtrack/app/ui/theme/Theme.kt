package com.dualtrack.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = FounderSeed,
    onPrimary = Color.White,
    primaryContainer = FounderContainer,
    onPrimaryContainer = FounderOnContainer,
    secondary = StudentSeed,
    onSecondary = Color.White,
    secondaryContainer = StudentContainer,
    onSecondaryContainer = StudentOnContainer,
    background = Color(0xFFFFFFFF),
    onBackground = Ink,
    surface = SurfaceTint,
    onSurface = Ink,
    surfaceVariant = Color(0xFFEEEEF2),
    onSurfaceVariant = MutedInk,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFC766),
    onPrimary = Color(0xFF4A2E00),
    primaryContainer = Color(0xFF5A3B00),
    onPrimaryContainer = Color(0xFFFFE3B0),
    secondary = Color(0xFF8AB4FF),
    onSecondary = Color(0xFF002855),
    secondaryContainer = Color(0xFF1B3A66),
    onSecondaryContainer = Color(0xFFD6E7FF),
    background = Color(0xFF121216),
    onBackground = Color(0xFFECECF1),
    surface = Color(0xFF1C1C21),
    onSurface = Color(0xFFECECF1),
    surfaceVariant = Color(0xFF2A2A31),
    onSurfaceVariant = Color(0xFFB4B4BF),
)

@Composable
fun DualTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
