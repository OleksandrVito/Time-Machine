package ua.vitolex.timecalculator.presentation.screen

import android.icu.util.Calendar
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.presentation.MainViewModel
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.components.CustomInput
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.presentation.navigation.Settings
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import ua.vitolex.timecalculator.ui.theme.cairo
import ua.vitolex.timecalculator.ui.theme.exo
import ua.vitolex.timecalculator.utils.DrawerEvents
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeConverterScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    settings: Settings,
    onEvent: (DrawerEvents) -> Unit,
    selectedTheme: AppTheme,
) {

    var yearsInputValue by remember {
        mutableStateOf("")
    }
    var monthsInputValue by remember {
        mutableStateOf("")
    }
    var weeksInputValue by remember {
        mutableStateOf("")
    }
    var daysInputValue by remember {
        mutableStateOf("")
    }
    var hoursInputValue by remember {
        mutableStateOf("")
    }
    var minutesInputValue by remember {
        mutableStateOf("")
    }

    var sec by remember {
        mutableStateOf(0L)
    }

    var visibleDifference by remember {
        mutableStateOf(
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toConvert(
        yearsInputValue: String,
        monthsInputValue: String,
        weeksInputValue: String,
        daysInputValue: String,
        hoursInputValue: String,
        minutesInputValue: String,
    ) {
        val minutes = try {
            minutesInputValue.toDouble() * 60
        } catch (e: NumberFormatException) {
            0.0
        }
        val hours = try {
            hoursInputValue.toDouble() * 60 * 60
        } catch (e: NumberFormatException) {
            0.0
        }
        val days = try {
            daysInputValue.toDouble() * 24 * 60 * 60
        } catch (e: NumberFormatException) {
            0.0
        }
        val weeks = try {
            weeksInputValue.toDouble() * 7 * 24 * 60 * 60
        } catch (e: NumberFormatException) {
            0.0
        }
        val months = try {
            monthsInputValue.toDouble() * settings.days.toDouble() * 24 * 60 * 60
        } catch (e: NumberFormatException) {
            0.0
        }
        val years = try {
            yearsInputValue.toDouble() * settings.years.toDouble() * 24 * 60 * 60
        } catch (e: NumberFormatException) {
            0.0
        }
        val seconds = minutes + hours + days + weeks + months + years

        sec = seconds.toLong()
    }


    val kc = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val title = stringResource(R.string.Time_machine)

    BackHandler(enabled = true, onBack = {
        onEvent(DrawerEvents.OnItemClick(title))
        navController.navigate(Screens.MainScreen.rout) { navController.popBackStack() }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(10.dp)
                .padding(top = 10.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column() {
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(100.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = yearsInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length <= 3) {
                                                    yearsInputValue = it
                                                }
                                                toConvert(
                                                    yearsInputValue,
                                                    monthsInputValue,
                                                    weeksInputValue,
                                                    daysInputValue,
                                                    hoursInputValue,
                                                    minutesInputValue,
                                                )
                                            }
                                        } catch (e: NumberFormatException) {
                                            if (it.length == 0) yearsInputValue = ""
                                            toConvert(
                                                yearsInputValue,
                                                monthsInputValue,
                                                weeksInputValue,
                                                daysInputValue,
                                                hoursInputValue,
                                                minutesInputValue,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .width(75.dp),
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
                                text = stringResource(R.string.Years),
                                fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp()
                            )

                        }
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(100.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = monthsInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length <= 4) {
                                                    monthsInputValue = it
                                                }
                                                toConvert(
                                                    yearsInputValue,
                                                    monthsInputValue,
                                                    weeksInputValue,
                                                    daysInputValue,
                                                    hoursInputValue,
                                                    minutesInputValue,
                                                )
                                            }
                                        } catch (e: NumberFormatException) {
                                            if (it.length == 0) monthsInputValue = ""
                                            toConvert(
                                                yearsInputValue,
                                                monthsInputValue,
                                                weeksInputValue,
                                                daysInputValue,
                                                hoursInputValue,
                                                minutesInputValue,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .width(75.dp),
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
                                text = stringResource(R.string.Months),
                                fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp()
                            )
                        }
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(100.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = weeksInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length <= 4) {
                                                    weeksInputValue = it
                                                }
                                                toConvert(
                                                    yearsInputValue,
                                                    monthsInputValue,
                                                    weeksInputValue,
                                                    daysInputValue,
                                                    hoursInputValue,
                                                    minutesInputValue,
                                                )
                                            }
                                        } catch (e: NumberFormatException) {
                                            if (it.length == 0) weeksInputValue = ""
                                            toConvert(
                                                yearsInputValue,
                                                monthsInputValue,
                                                weeksInputValue,
                                                daysInputValue,
                                                hoursInputValue,
                                                minutesInputValue,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .width(75.dp),
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
                            Text(text = stringResource(R.string.Weeks),fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp())
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(100.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = daysInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length <= 5) {
                                                    daysInputValue = it
                                                }
                                                toConvert(
                                                    yearsInputValue,
                                                    monthsInputValue,
                                                    weeksInputValue,
                                                    daysInputValue,
                                                    hoursInputValue,
                                                    minutesInputValue,
                                                )
                                            }
                                        } catch (e: NumberFormatException) {
                                            if (it.length == 0) daysInputValue = ""
                                            toConvert(
                                                yearsInputValue,
                                                monthsInputValue,
                                                weeksInputValue,
                                                daysInputValue,
                                                hoursInputValue,
                                                minutesInputValue,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .width(75.dp),
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
                            Text(text = stringResource(R.string.Days), fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp())
                        }
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(100.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = hoursInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length <= 5) {
                                                    hoursInputValue = it
                                                }
                                                toConvert(
                                                    yearsInputValue,
                                                    monthsInputValue,
                                                    weeksInputValue,
                                                    daysInputValue,
                                                    hoursInputValue,
                                                    minutesInputValue,
                                                )
                                            }
                                        } catch (e: NumberFormatException) {
                                            if (it.length == 0) hoursInputValue = ""
                                            toConvert(
                                                yearsInputValue,
                                                monthsInputValue,
                                                weeksInputValue,
                                                daysInputValue,
                                                hoursInputValue,
                                                minutesInputValue,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .width(75.dp),
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
                            Text(text = stringResource(R.string.Hours), fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp())
                        }
                        Box(
                            modifier = Modifier.height(100.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(100.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = minutesInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length <= 5) {
                                                    minutesInputValue = it
                                                }
                                                toConvert(
                                                    yearsInputValue,
                                                    monthsInputValue,
                                                    weeksInputValue,
                                                    daysInputValue,
                                                    hoursInputValue,
                                                    minutesInputValue,
                                                )
                                            }
                                        } catch (e: NumberFormatException) {
                                            if (it.length == 0) minutesInputValue = ""
                                            toConvert(
                                                yearsInputValue,
                                                monthsInputValue,
                                                weeksInputValue,
                                                daysInputValue,
                                                hoursInputValue,
                                                minutesInputValue,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .width(75.dp),
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
                            Text(text = stringResource(R.string.Minutes), fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp())
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Row() {
                    Text(
                        text = stringResource(id = R.string.EQUALS) +
                                "\n ${sec / 60 / 60.toFloat()} " + stringResource(R.string.hours) +
                                "\n= ${sec / 60 / 60 / 24.toFloat() / settings.years.toFloat()} " + stringResource(
                            R.string.years
                        ) +
                                "\n= ${sec / 60 / 60 / 24.toFloat() / settings.days.toFloat()} " + stringResource(
                            R.string.months
                        ) +
                                "\n= ${sec / 60 / 60 / 24.toFloat()} " + stringResource(R.string.days) +
                                "\n= ${sec / 60} " + stringResource(R.string.minutes) +
                                "\n= ${sec} " + stringResource(R.string.seconds),
                        maxLines = if (visibleDifference) 8 else 2,
                        overflow = TextOverflow.Clip,
                        lineHeight = 32.scaledSp(),
                        fontSize = 18.scaledSp(),
                        color = Color.Red.copy(0.7f),
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    IconButton(onClick = { visibleDifference = !visibleDifference }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Toggle visible difference",
                            tint = GreyTextColor,
                            modifier = Modifier
                                .rotate(if (visibleDifference) 180f else 0f)
                                .scale(1.5f)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.heightIn(min = 60.dp), contentAlignment = Alignment.Center) {
            BannerAdView(id = stringResource(id = R.string.main_banner5))
        }
    }
}