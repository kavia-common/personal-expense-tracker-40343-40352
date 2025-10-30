package com.smartspender.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = OceanPrimary,
    secondary = OceanSecondary,
    error = OceanError,
    background = OceanBackground,
    surface = OceanSurface,
    onPrimary = OceanSurface,
    onSecondary = OceanText,
    onError = OceanSurface,
    onBackground = OceanText,
    onSurface = OceanText
)

private val DarkColors = darkColorScheme(
    primary = OceanPrimary,
    secondary = OceanSecondary,
    error = OceanError
)

@Composable
// PUBLIC_INTERFACE
fun SmartSpenderTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (useDarkTheme) DarkColors else LightColors,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
