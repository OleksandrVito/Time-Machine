package ua.vitolex.timecalculator.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import ua.vitolex.timecalculator.presentation.screen.*
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.utils.DrawerEvents

sealed class Screens(val rout: String) {
    object SplashScreen : Screens(rout = "splash_screen")
    object MainScreen : Screens(rout = "main_screen")
    object AddOrSubTimeScreen : Screens(rout = "addOrSubTime_screen")
    object TimeDifference : Screens(rout = "timeDifference_screen")
    object SettingsScreen : Screens(rout = "settings_screen")
}

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavHost(
    navController: NavHostController,
    settings: Settings,
    onEvent: (DrawerEvents) -> Unit,
    selectedTheme: AppTheme,
    onItemSelected: (AppTheme) -> Unit,
) {
    AnimatedNavHost(navController = navController, startDestination = Screens.MainScreen.rout) {
        composable(route = Screens.SplashScreen.rout,
            exitTransition = {
                // Let's make for a really long fade in
                fadeOut(animationSpec = tween(250))
            }
        ) {
            SplashScreen(navController = navController, onEvent = onEvent)
        }
        composable(route = Screens.MainScreen.rout,
            exitTransition = {
                // Let's make for a really long fade in
                fadeOut(animationSpec = tween(250))
            }) {
            MainScreen(navController = navController)
        }
        composable(route = Screens.AddOrSubTimeScreen.rout,
            exitTransition = {
                // Let's make for a really long fade in
                fadeOut(animationSpec = tween(250))
            }) {
            AddOrSubTimeScreen(
                navController = navController,
                settings = settings,
                onEvent = onEvent,
                selectedTheme = selectedTheme,
            )
        }
        composable(route = Screens.TimeDifference.rout,
            exitTransition = {
                // Let's make for a really long fade in
                fadeOut(animationSpec = tween(250))
            }) {
            TimeDifferenceScreen(
                navController = navController,
                settings = settings,
                onEvent = onEvent,
                selectedTheme = selectedTheme,
            )
        }
        composable(route = Screens.SettingsScreen.rout,
            exitTransition = {
                // Let's make for a really long fade in
                fadeOut(animationSpec = tween(250))
            }) {
            SettingsScreen(
                navController = navController,
                settings = settings, onEvent = onEvent,
                selectedTheme = selectedTheme,
                onItemSelected = onItemSelected,
            )
        }
    }
}

data class Settings(
    val days: String,
    val years: String,
)