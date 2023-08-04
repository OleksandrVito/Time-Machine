package ua.vitolex.timecalculator.presentation.screen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.dataStore
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.components.CustomInput
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.presentation.navigation.Settings
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.settings.RadioButtonItem
import ua.vitolex.timecalculator.settings.RadioGroup
import ua.vitolex.timecalculator.utils.DrawerEvents


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    settings: Settings,
    onEvent: (DrawerEvents) -> Unit,
    selectedTheme: AppTheme,
    onItemSelected: (AppTheme) -> Unit,
) {
    var context = LocalContext.current
    var scope = rememberCoroutineScope()

    var inputDaysSetting by rememberSaveable {
        mutableStateOf(settings.days)
    }

    var inputYearsSetting by rememberSaveable {
        mutableStateOf(settings.years)
    }

    val kc = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val title = stringResource(R.string.Time_machine)

    BackHandler(enabled = true, onBack = {
        onEvent(DrawerEvents.OnItemClick(title))
        navController.navigate(Screens.MainScreen.rout) { navController.popBackStack() }
    })

    val themeItems = listOf(
        RadioButtonItem(
            id = AppTheme.MODE_DAY.ordinal,
            title = stringResource(R.string.Light_theme),
        ),
        RadioButtonItem(
            id = AppTheme.MODE_NIGHT.ordinal,
            title = stringResource(R.string.Dark_theme),
        ),
        RadioButtonItem(
            id = AppTheme.MODE_AUTO.ordinal,
            title = stringResource(R.string.Auto_theme),
        ),
    )


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
       LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(top = 10.dp).weight(1f)
        ) {
           item {
               Column(
                   modifier = Modifier
                       .fillMaxWidth()
               ) {
                   Row(
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(horizontal = 15.dp, vertical = 0.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text(
                           text = stringResource(R.string.Days_in_a_month), fontSize = 20.scaledSp(),
                           textAlign = TextAlign.Center,
                           modifier = Modifier
                               .padding(top = 35.dp)
                       )
                       CustomInput(
                           value = inputDaysSetting,
                           onValueChange = {
                               try {
                                   it.toDouble()
                                   if (it.length < 7) {
                                       inputDaysSetting = it
                                       if (it.toFloat() in 30f..31f)
                                           scope.launch {
                                               saveDays(context, it)
                                           }
                                   }
                               } catch (e: NumberFormatException) {
                                   if (it.length < 7) {
                                       inputDaysSetting = it
                                   }
                               }
                           },
                           modifier = Modifier
                               .width(75.dp)
                               .height(70.dp),
                           keyboardOptions = KeyboardOptions(
                               keyboardType = KeyboardType.Number,
                               autoCorrect = true,
                               imeAction = ImeAction.Done
                           ),
                           keyboardActions = KeyboardActions(
                               onDone = {
                                   focusManager.clearFocus()
                                   kc?.hide()
                               }
                           ),
                       )
                   }
                   Text(text = stringResource(R.string.By_default_days),modifier = Modifier
                       .fillMaxWidth(),
                       textAlign = TextAlign.Center, fontSize = 16.scaledSp())
               }
           }
           item {
               Column(
                   modifier = Modifier
                       .fillMaxWidth()
               ) {
                   Row(
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(horizontal = 15.dp, vertical = 0.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text(
                           text = stringResource(R.string.Days_in_a_year), fontSize = 20.scaledSp(),
                           textAlign = TextAlign.Center,
                           modifier = Modifier
                               .padding(top = 35.dp)
                       )
                       CustomInput(
                           value = inputYearsSetting,
                           onValueChange = {
                               try {
                                   it.toDouble()
                                   if (it.length < 7) {
                                       inputYearsSetting = it
                                       if (it.toFloat() in 365f..366f)
                                           scope.launch {
                                               saveYears(context, it)
                                           }
                                   }
                               } catch (e: NumberFormatException) {
                                   if (it.length < 7) {
                                       inputYearsSetting = it
                                   }
                               }
                           },
                           modifier = Modifier
                               .width(75.dp)
                               .height(70.dp),
                           keyboardOptions = KeyboardOptions(
                               keyboardType = KeyboardType.Number,
                               autoCorrect = true,
                               imeAction = ImeAction.Done
                           ),
                           keyboardActions = KeyboardActions(
                               onDone = {
                                   focusManager.clearFocus()
                                   kc?.hide()
                               }
                           ),
                       )
                   }
                   Text(
                       text = stringResource(R.string.By_default_days_in_year),
                       modifier = Modifier
                           .fillMaxWidth(),
                       textAlign = TextAlign.Center,fontSize = 16.scaledSp()
                   )
               }
           }
           item {
               Column(
                   modifier = Modifier.fillMaxSize().padding(all = 15.dp),
                   horizontalAlignment = Alignment.Start,
               ) {
                   Text(
                       text = stringResource(R.string.Choose_your_theme),
                       fontSize = 20.scaledSp(),
                       textAlign = TextAlign.Start,
                       modifier = Modifier
                           .padding(top = 35.dp)
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   RadioGroup(
                       items = themeItems,
                       selected = selectedTheme.ordinal,
                       onItemSelect = { id -> onItemSelected(AppTheme.fromOrdinal(id)) },
                       modifier = Modifier.fillMaxWidth(),
                   )
               }
           }
        }
        BannerAdView(id = stringResource(id = R.string.main_banner2))
    }
}

suspend fun saveDays(context: Context, days: String) {
    val key = stringPreferencesKey("days")
    context.dataStore.edit {
        it[key] = days
    }
}

suspend fun saveYears(context: Context, years: String) {
    val key = stringPreferencesKey("years")
    context.dataStore.edit {
        it[key] = years
    }
}
