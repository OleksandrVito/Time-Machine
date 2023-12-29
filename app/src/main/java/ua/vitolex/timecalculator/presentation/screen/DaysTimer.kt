package ua.vitolex.timecalculator.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.dataStore
import ua.vitolex.timecalculator.presentation.MainViewModel
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.components.CustomInput
import ua.vitolex.timecalculator.presentation.components.Dotes
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.presentation.navigation.Settings
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.shadow
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import ua.vitolex.timecalculator.ui.theme.cairo
import ua.vitolex.timecalculator.ui.theme.exo
import ua.vitolex.timecalculator.utils.DrawerEvents
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysTimerScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    settings: Settings,
    onEvent: (DrawerEvents) -> Unit,
    selectedTheme: AppTheme,
    timeToEndTimer: String,
) {
    var context = LocalContext.current
    var scope = rememberCoroutineScope()

    val calendarFrom = viewModel.calendarFrom

    val yearFrom = calendarFrom.get(Calendar.YEAR)
    val monthFrom = calendarFrom.get(Calendar.MONTH)
    val dayOfMonthFrom = calendarFrom.get(Calendar.DAY_OF_MONTH)

    var hourFrom = calendarFrom.get(Calendar.HOUR_OF_DAY)
    var minuteFrom = calendarFrom.get(Calendar.MINUTE)

    var calendarCurrent = viewModel.calendarCurrent

    val year = calendarCurrent.get(Calendar.YEAR)
    val month = calendarCurrent.get(Calendar.MONTH)
    val dayOfMonth = calendarCurrent.get(Calendar.DAY_OF_MONTH)

    var hour = calendarCurrent.get(Calendar.HOUR_OF_DAY)
    var minute = calendarCurrent.get(Calendar.MINUTE)

    val formatDate = DateTimeFormatter.ofPattern("EE, dd LLL, yyyy")
    val formatTime = DateTimeFormatter.ofPattern("HH:mm")

    var fromDate by remember {
        mutableStateOf(
            LocalDate.of(
                yearFrom,
                monthFrom + 1,
                dayOfMonthFrom
            )
        )
    }
    var fromTime by remember { mutableStateOf(LocalTime.now()) }
    var fromDateTime by remember { mutableStateOf(LocalDateTime.of(fromDate, fromTime)) }

    var operation by remember {
        mutableStateOf("plus")
    }
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

    var andDateTime by remember { mutableStateOf( if (timeToEndTimer!="") LocalDateTime.parse(timeToEndTimer) else LocalDateTime.of(fromDate, fromTime)) }

    fun getDifference(
        fromDateTime: LocalDateTime,
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
        val secondsToAdd = minutes + hours + days + weeks + months + years

        if (operation == "plus") {
            andDateTime = fromDateTime.plus(secondsToAdd.toLong(), ChronoUnit.SECONDS)
        } else andDateTime = fromDateTime.minus(secondsToAdd.toLong(), ChronoUnit.SECONDS)
        scope.launch {
            saveTime (context = context, time = andDateTime.toString())
        }
    }

    val datePicker = DatePickerDialog(
        context,
        if (selectedTheme.ordinal == 1 || isSystemInDarkTheme()) R.style.MyDatePickerDialogDarkTheme else R.style.MyDatePickerDialogTheme,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            fromDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
            fromDateTime = LocalDateTime.of(fromDate, fromTime)
            calendarFrom.set(selectedYear, selectedMonth, selectedDayOfMonth)
            getDifference(
                fromDateTime,
                yearsInputValue,
                monthsInputValue,
                weeksInputValue,
                daysInputValue,
                hoursInputValue,
                minutesInputValue,
            )
        }, yearFrom, monthFrom, dayOfMonthFrom
    )


    val timePicker = TimePickerDialog(
        context,
        if (selectedTheme.ordinal == 1 || isSystemInDarkTheme()) R.style.MyDatePickerDialogDarkTheme else R.style.MyDatePickerDialogTheme,
        { _, selectedHour: Int, selectedMinute: Int ->
            fromTime = LocalTime.of(selectedHour, selectedMinute)
            fromDateTime = LocalDateTime.of(fromDate, fromTime)
            calendarFrom.set(yearFrom, monthFrom, dayOfMonthFrom, selectedHour, selectedMinute)
            getDifference(
                fromDateTime,
                yearsInputValue,
                monthsInputValue,
                weeksInputValue,
                daysInputValue,
                hoursInputValue,
                minutesInputValue,
            )
        }, hourFrom, minuteFrom, true
    )

    val kc = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val title = stringResource(R.string.Time_machine)

    var color by remember {
        mutableStateOf(GreyTextColor)
    }

    var currentDateTime by remember {
        mutableStateOf(LocalDateTime.now())
    }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000)
            currentDateTime = LocalDateTime.now()
        }
    }

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
                Spacer(modifier = Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Box(modifier = Modifier
                        .width(180.dp)
                        .shadow(
                            color = MaterialTheme.colors.onPrimary,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 10.dp
                        )
                        .shadow(
                            color = MaterialTheme.colors.onSecondary,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp
                        )
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colors.primary)
                        .clickable {
                            datePicker.show()
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fromDate.format(formatDate),
                            fontSize = 16.scaledSp(), color = GreyTextColor
                        )
                    }
                    Box(modifier = Modifier
                        .width(80.dp)
                        .shadow(
                            color = MaterialTheme.colors.onPrimary,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 10.dp
                        )
                        .shadow(
                            color = MaterialTheme.colors.onSecondary,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp
                        )
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colors.primary)
                        .clickable {
                            timePicker.show()
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fromTime.format(formatTime),
                            fontSize = 16.scaledSp(),
                            color = GreyTextColor
                        )
                    }
                    Box(modifier = Modifier
                        .width(80.dp)
                        .shadow(
                            color = MaterialTheme.colors.onPrimary,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 10.dp
                        )
                        .shadow(
                            color = MaterialTheme.colors.onSecondary,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp
                        )
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colors.primary)
                        .clickable {
                            calendarFrom.set(year, month, dayOfMonth)
                            fromDate = LocalDate.of(year, month + 1, dayOfMonth)
                            fromTime = LocalTime.now()
                            fromDateTime = LocalDateTime.of(fromDate, fromTime)
                            getDifference(
                                fromDateTime,
                                yearsInputValue,
                                monthsInputValue,
                                weeksInputValue,
                                daysInputValue,
                                hoursInputValue,
                                minutesInputValue,
                            )
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.NOW),
                            fontSize = 16.scaledSp(),
                            color = GreyTextColor
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier
                        .circleShadow(
                            color = MaterialTheme.colors.onPrimary,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 10.dp,
                            radius = 0f
                        )
                        .circleShadow(
                            color = MaterialTheme.colors.onSecondary,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp,
                            radius = 0f
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colors.primary)
                        .clickable {
                            operation = "plus"
                            getDifference(
                                fromDateTime,
                                yearsInputValue,
                                monthsInputValue,
                                weeksInputValue,
                                daysInputValue,
                                hoursInputValue,
                                minutesInputValue,
                            )
                        }
                        .height(48.dp)
                        .width(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+", color = if (operation == "plus") {
                                Color.Red.copy(0.7f)
                            } else {
                                GreyTextColor
                            }, fontSize = 26.scaledSp()
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Box(modifier = Modifier
                        .circleShadow(
                            color = MaterialTheme.colors.onPrimary,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 10.dp,
                            radius = 0f
                        )
                        .circleShadow(
                            color = MaterialTheme.colors.onSecondary,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp,
                            radius = 0f
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colors.primary)
                        .clickable {
                            operation = "minus"
                            getDifference(
                                fromDateTime,
                                yearsInputValue,
                                monthsInputValue,
                                weeksInputValue,
                                daysInputValue,
                                hoursInputValue,
                                minutesInputValue,
                            )
                        }
                        .height(48.dp)
                        .width(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            color = if (operation == "minus") {
                                Color.Red.copy(0.7f)
                            } else {
                                GreyTextColor
                            },
                            fontSize = 30.scaledSp(),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
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
                                                if (it.length < 4) {
                                                    yearsInputValue = it
                                                }
                                                getDifference(
                                                    fromDateTime,
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
                                            getDifference(
                                                fromDateTime,
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
                                text = stringResource(R.string.Years), fontSize = 14.scaledSp(),
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
                                                if (it.length < 5) {
                                                    monthsInputValue = it
                                                }
                                                getDifference(
                                                    fromDateTime,
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
                                            getDifference(
                                                fromDateTime,
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
                                text = stringResource(R.string.Months), fontSize = 14.scaledSp(),
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
                                                if (it.length < 5) {
                                                    weeksInputValue = it
                                                }
                                                getDifference(
                                                    fromDateTime,
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
                                            getDifference(
                                                fromDateTime,
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
                                text = stringResource(R.string.Weeks), fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp()
                            )
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
                                                if (it.length < 6) {
                                                    daysInputValue = it
                                                }
                                                getDifference(
                                                    fromDateTime,
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
                                            getDifference(
                                                fromDateTime,
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
                                text = stringResource(R.string.Days), fontSize = 14.scaledSp(),
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
                                    value = hoursInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length < 6) {
                                                    hoursInputValue = it
                                                }
                                                getDifference(
                                                    fromDateTime,
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
                                            getDifference(
                                                fromDateTime,
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
                                text = stringResource(R.string.Hours), fontSize = 14.scaledSp(),
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
                                    value = minutesInputValue,
                                    onValueChange = {
                                        try {
                                            if (!it.endsWith(" ")) {
                                                it.toDouble()
                                                if (it.length < 6) {
                                                    minutesInputValue = it
                                                }
                                                getDifference(
                                                    fromDateTime,
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
                                            getDifference(
                                                fromDateTime,
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
                                        keyboardType = KeyboardType.Decimal,
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
                                text = stringResource(R.string.Minutes), fontSize = 14.scaledSp(),
                                letterSpacing = 2.scaledSp()
                            )
                        }
                    }
                }
            }
            item {
                val formatDate = DateTimeFormatter.ofPattern("HH:mm, EE, dd LLL, yyyy")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = andDateTime.format(formatDate),
                    fontSize = 20.scaledSp(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = exo,
                    color = Color.Red.copy(0.7f)
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                val time = viewModel.getDifference(currentDateTime, andDateTime)
                Row(
                    Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    if (time.days <= 0 && time.hour <= 0 && time.minute <= 0 && time.second <= 0) {
                        Text(
                            text = stringResource(R.string.passed),
                            fontSize = 20.scaledSp(),
                            fontFamily = exo,
                            color = Color.Red.copy(0.7f),
                            fontWeight = FontWeight.Medium,
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.remaining),
                            fontSize = 20.scaledSp(),
                            fontFamily = exo,
                            color = GreyTextColor,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    val time = viewModel.getDifference(currentDateTime, andDateTime)

                    if (time.days <= 0 && time.hour <= 0 && time.minute <= 0 && time.second <= 0) {
                        color = Color.Red.copy(0.7f)
                    } else {
                        color = GreyTextColor
                    }
                    Text(
                        text = if (time.days < 0) "${-time.days}" else "${time.days}",
                        fontSize = 22.scaledSp(),
                        fontFamily = cairo,
                        color = color,
                    )

                    Dotes(color)
                    Text(
                        text = if (time.hour < -9) "${-time.hour}" else if (time.hour in (-9..0)) "0${-time.hour}" else if (time.hour < 10) "0${time.hour}" else "${time.hour}",
                        fontSize = 22.scaledSp(),
                        fontFamily = cairo,
                        color = color,
                    )

                    Dotes(color)
                    Text(
                        text = if (time.minute < -9) "${-time.minute}" else if (time.minute in (-9..0)) "0${-time.minute}" else if (time.minute < 10) "0${time.minute}" else "${time.minute}",
                        fontSize = 22.scaledSp(),
                        fontFamily = cairo,
                        color = color,
                    )
                    Dotes(color)

                    Text(
                        text =
                        if (time.second < -9) "${-time.second}" else if (time.second in (-9..0)) "0${-time.second}" else if (time.second < 10) "0${time.second}" else "${time.second}",
                        fontSize = 22.scaledSp(),
                        fontFamily = cairo,
                        color = color,
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.heightIn(min = 60.dp), contentAlignment = Alignment.Center) {
            BannerAdView(id = stringResource(id = R.string.main_banner6))
        }
    }
}

suspend fun saveTime(context: Context, time: String) {
    val key = stringPreferencesKey("time")
    context.dataStore.edit {
        it[key] = time
    }
}