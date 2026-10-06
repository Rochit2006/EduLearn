package com.example.edulearn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EduLearnDarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    secondary = Color(0xFFA78BFA),
    tertiary = Color(0xFFF472B6),

    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val EduLearnLightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),
    secondary = Color(0xFF6366F1),
    tertiary = Color(0xFFDB2777),

    background = Color(0xFFF7F9FC),
    surface = Color.White,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF172554),
    onSurface = Color(0xFF172554)
)

@Composable
fun EduLearnTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        EduLearnDarkColorScheme
    } else {
        EduLearnLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}