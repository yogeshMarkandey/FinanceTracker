package com.example.financetracker.presentation.ui.theme

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
    primary = Color(0xFF665596),
    secondary = Color(0xFF5D5D71),
    tertiary = Color(0xFF86458B),//
    onPrimary = Color(0xFFFEFEFF),
    onSecondary = Color(0xFFFEFEFF),
    onTertiary = Color(0xFFFEFEFF),
    background = Color.Black.copy(alpha = 0.35f),
    onPrimaryContainer = Color(0xFF4E3B7B),
    primaryContainer = Color(0xFFC9B2FD),
    onTertiaryContainer = Color(0xFF6A2D72),
    onBackground = Color.White,
    secondaryContainer = Color(0xFFE3E0F8),
    tertiaryContainer = Color(0xFFFBAFFD),
    onSecondaryContainer = Color(0xFF4E3B7B),
    surface = Color(0xFFFFF7FF),
    onSurface = Color(0xFF1C1A20),
    inverseSurface = Color(0xFF302E35),
    inverseOnSurface = Color(0xFFF5EFF6),
    inversePrimary = Color(0xFFD0BDFC),
    onSurfaceVariant = Color(0xFF49454C),
    surfaceVariant = Color(0xFFDED8DF),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFED8D6),
    onError = Color(0xFFFEFEFF),
    onErrorContainer = Color(0xFF7D2A25),
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}