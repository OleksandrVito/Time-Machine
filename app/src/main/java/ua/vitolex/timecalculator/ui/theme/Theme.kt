package ua.vitolex.timecalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DarkBackground,
    secondary = GreyTextColor,
    onPrimary = DarkShadow1,
    onSecondary = DarkShadow2,
    onSurface = DarkShadow2.copy(0.1f)
)

private val LightColorPalette = lightColors(
    primary = LightBackground,
    secondary = GreyTextColor,
    onPrimary = LightShadow1,
    onSecondary = LightShadow2,
    onSurface = Color.White.copy(0.6f)


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TimeCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = DarkBackground.copy(0.9f),
            darkIcons = true,
        )
        systemUiController.setNavigationBarColor(
            color = DarkBackground.copy(0.9f),
            darkIcons = false
        )
    } else {
        systemUiController.setStatusBarColor(
            color = LightBackground.copy(0.4f),
            darkIcons = true,
        )
        systemUiController.setNavigationBarColor(
            color = LightBackground.copy(0.4f),
            darkIcons = true
        )
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}