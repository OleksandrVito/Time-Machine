package ua.vitolex.timecalculator

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.vitolex.timecalculator.presentation.components.DrawerBody
import ua.vitolex.timecalculator.presentation.components.DrawerHeader
import ua.vitolex.timecalculator.presentation.components.MenuItem
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.presentation.navigation.Settings
import ua.vitolex.timecalculator.presentation.navigation.SetupNavHost
import ua.vitolex.timecalculator.ui.theme.TimeCalculatorTheme
import ua.vitolex.timecalculator.presentation.components.AppBar
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.settings.UserSettings
import ua.vitolex.timecalculator.ui.theme.LightBackground
import ua.vitolex.timecalculator.utils.DrawerEvents
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userSettings: UserSettings

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(
            savedInstanceState
        )
        setContent {
            val navController = rememberAnimatedNavController()
            val keyDays = stringPreferencesKey("days")
            val keyYears = stringPreferencesKey("years")

            val theme = userSettings.themeStream.collectAsState()
            val useDarkColors = when (theme.value) {
                AppTheme.MODE_AUTO -> isSystemInDarkTheme()
                AppTheme.MODE_DAY -> false
                AppTheme.MODE_NIGHT -> true
            }

            val days =
                this.dataStore.data.map {
                    it[keyDays]
                }.collectAsState(initial = "")

            val years =
                this.dataStore.data.map {
                    it[keyYears]
                }.collectAsState(initial = "")


            TimeCalculatorTheme(
                darkTheme = useDarkColors
            ) {
                val scaffoldState = rememberScaffoldState()
                val topBarTitle = remember {
                    mutableStateOf("")
                }
                val scope = rememberCoroutineScope()



                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                            AppBar(
                                title = topBarTitle.value,
                                onNavigationIconClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }
                            )
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    title = "Home",
                                    route = Screens.MainScreen.rout
                                ),
                                MenuItem(
                                    title = "Time difference",
                                    route = Screens.TimeDifference.rout
                                ),
                                MenuItem(
                                    title = "Add or sub time",
                                    route = Screens.AddOrSubTimeScreen.rout
                                ),
                                MenuItem(
                                    title = "Settings",
                                    route = Screens.SettingsScreen.rout
                                ),
                            ),
                            navController = navController,
                            closeNavDrawer = {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            },
                            onEvent = { event ->
                                when (event) {
                                    is DrawerEvents.OnItemClick -> {
                                        topBarTitle.value = event.title
                                    }
                                    else -> {}
                                }
                            },
                            modifier = Modifier.padding(top = 40.dp)
                        )
                    },
                    drawerBackgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .background(LightBackground)
                        .padding(
                          0.dp
                        ),
                    backgroundColor = MaterialTheme.colors.primary,
                ) {
                    SetupNavHost(
                        navController = navController,
                        settings = Settings(
                            days.value ?: "30.42",
                            years.value ?: "365.25"
                        ),
                        onEvent = { event ->
                            when (event) {
                                is DrawerEvents.OnItemClick -> {
                                    topBarTitle.value = event.title
                                }
                            }
                        },
                        selectedTheme = theme.value,
                        onItemSelected = { theme -> userSettings.theme = theme },
                    )
                }
            }
        }
//        MobileAds.initialize(this)
    }
}
