package ua.vitolex.timecalculator.presentation.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.presentation.MainViewModel
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.components.CustomInput
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.presentation.navigation.Settings
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.shadow
import ua.vitolex.timecalculator.ui.theme.*
import ua.vitolex.timecalculator.utils.DrawerEvents
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddOrSubTimeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    settings: Settings,
    onEvent: (DrawerEvents) -> Unit,
    selectedTheme: AppTheme,
) {
    val context = LocalContext.current

    val calendarFrom = viewModel.calendarFrom

    val yearFrom = calendarFrom.get(Calendar.YEAR)
    val monthFrom = calendarFrom.get(Calendar.MONTH)
    val dayOfMonthFrom = calendarFrom.get(Calendar.DAY_OF_MONTH)

    val hourFrom = calendarFrom.get(Calendar.HOUR_OF_DAY)
    val minuteFrom = calendarFrom.get(Calendar.MINUTE)

    val calendarCurrent = viewModel.calendarCurrent

    val year = calendarCurrent.get(Calendar.YEAR)
    val month = calendarCurrent.get(Calendar.MONTH)
    val dayOfMonth = calendarCurrent.get(Calendar.DAY_OF_MONTH)

    val hour = calendarCurrent.get(Calendar.HOUR_OF_DAY)
    val minute = calendarCurrent.get(Calendar.MINUTE)

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
    var fromTime by remember { mutableStateOf(LocalTime.of(hourFrom, minuteFrom)) }
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

    var andDateTime by remember { mutableStateOf(LocalDateTime.of(fromDate, fromTime)) }

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
                            fromTime = LocalTime.of(hour, minute)
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
                        Text(text = stringResource(R.string.NOW), fontSize = 16.scaledSp(), color = GreyTextColor)
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
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(110.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = yearsInputValue,
                                    onValueChange = {
                                        try {
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
                                        } catch (e: NumberFormatException) {
                                            if (it.length < 4) {
                                                yearsInputValue = it
                                            }
                                            getDifference(
                                                fromDateTime,
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
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
                            Text(text = stringResource(R.string.Years), fontSize = 20.scaledSp())

                        }
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(110.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = monthsInputValue,
                                    onValueChange = {
                                        try {
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
                                        } catch (e: NumberFormatException) {
                                            if (it.length < 5) {
                                                monthsInputValue = it
                                            }
                                            getDifference(
                                                fromDateTime,
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
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
                            Text(text = stringResource(R.string.Months), fontSize = 20.scaledSp())
                        }
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(110.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = weeksInputValue,
                                    onValueChange = {
                                        try {
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
                                        } catch (e: NumberFormatException) {
                                            if (it.length < 5) {
                                                weeksInputValue = it
                                            }
                                            getDifference(
                                                fromDateTime,
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
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
                            Text(text = stringResource(R.string.Weeks), fontSize = 20.scaledSp())
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(110.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = daysInputValue,
                                    onValueChange = {
                                        try {
                                            it.toDouble()
                                            if (it.length < 7) {
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
                                        } catch (e: NumberFormatException) {
                                            if (it.length < 7) {
                                                daysInputValue = it
                                            }
                                            getDifference(
                                                fromDateTime,
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
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
                            Text(text = stringResource(R.string.Days), fontSize = 20.scaledSp())
                        }
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(110.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = hoursInputValue,
                                    onValueChange = {
                                        try {
                                            it.toDouble()
                                            if (it.length < 7) {
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
                                        } catch (e: NumberFormatException) {
                                            if (it.length < 7) {
                                                hoursInputValue = it
                                            }
                                            getDifference(
                                                fromDateTime,
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
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
                            Text(text = stringResource(R.string.Hours), fontSize = 20.scaledSp())
                        }
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier.height(110.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CustomInput(
                                    value = minutesInputValue,
                                    onValueChange = {
                                        try {
                                            it.toDouble()
                                            if (it.length < 7) {
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
                                        } catch (e: NumberFormatException) {
                                            if (it.length < 7) {
                                                minutesInputValue = it
                                            }
                                            getDifference(
                                                fromDateTime,
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
                                                "0",
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
                            Text(text = stringResource(R.string.Minutes), fontSize = 20.scaledSp())
                        }
                    }
                }
            }
            item {
                val formatDate = DateTimeFormatter.ofPattern("HH:mm, EE, dd LLL, yyyy")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = andDateTime.format(formatDate),
                    fontSize = 24.scaledSp(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Red.copy(0.7f)
                )
            }
        }
        BannerAdView(id = stringResource(id = R.string.main_banner4))
    }
}

