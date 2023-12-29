package ua.vitolex.timecalculator.presentation.screen

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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.presentation.MainViewModel
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.presentation.navigation.Settings
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.settings.AppTheme
import ua.vitolex.timecalculator.shadow
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import ua.vitolex.timecalculator.utils.DrawerEvents
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeDifferenceScreen(
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

    val calendarTo = viewModel.calendarTo

    val yearTo = calendarTo.get(Calendar.YEAR)
    val monthTo = calendarTo.get(Calendar.MONTH)
    val dayOfMonthTo = calendarTo.get(Calendar.DAY_OF_MONTH)

    val hourTo = calendarTo.get(Calendar.HOUR_OF_DAY)
    val minuteTo = calendarTo.get(Calendar.MINUTE)

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

    var toDate by remember { mutableStateOf(LocalDate.of(yearTo, monthTo + 1, dayOfMonthTo)) }
    var toTime by remember { mutableStateOf(LocalTime.of(hourTo, minuteTo)) }
    var toDateTime by remember { mutableStateOf(LocalDateTime.of(toDate, toTime)) }

    var timeDifference by remember {
        mutableStateOf(
            0L
        )
    }
    var visibleDifference by remember {
        mutableStateOf(
            false
        )
    }

    fun getDifference(fromDateTime: LocalDateTime, toDateTime: LocalDateTime) {
        timeDifference = Duration.between(fromDateTime, toDateTime).toMillis()
    }

    val datePickerFrom = DatePickerDialog(
        context,
        if (selectedTheme.ordinal == 1 || isSystemInDarkTheme()) R.style.MyDatePickerDialogDarkTheme else R.style.MyDatePickerDialogTheme,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            fromDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
            fromDateTime = LocalDateTime.of(fromDate, fromTime)
            getDifference(fromDateTime, toDateTime)
            calendarFrom.set(selectedYear, selectedMonth, selectedDayOfMonth)
        }, yearFrom, monthFrom, dayOfMonthFrom
    )

    val timePickerFrom = TimePickerDialog(
        context,
        if (selectedTheme.ordinal == 1 || isSystemInDarkTheme()) R.style.MyDatePickerDialogDarkTheme else R.style.MyDatePickerDialogTheme,
        { _, selectedHour: Int, selectedMinute: Int ->
            fromTime = LocalTime.of(selectedHour, selectedMinute)
            fromDateTime = LocalDateTime.of(fromDate, fromTime)
            getDifference(fromDateTime, toDateTime)
            calendarFrom.set(yearFrom, monthFrom, dayOfMonthFrom,selectedHour,selectedMinute)
        }, hourFrom, minuteFrom, true
    )

    val datePickerTo = DatePickerDialog(
        context,
        if (selectedTheme.ordinal == 1 || isSystemInDarkTheme()) R.style.MyDatePickerDialogDarkTheme else R.style.MyDatePickerDialogTheme,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            toDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
            toDateTime = LocalDateTime.of(toDate, toTime)
            getDifference(fromDateTime, toDateTime)
            calendarTo.set(selectedYear, selectedMonth, selectedDayOfMonth)
        }, yearTo, monthTo, dayOfMonthTo
    )

    val timePickerTo = TimePickerDialog(
        context,
        if (selectedTheme.ordinal == 1 || isSystemInDarkTheme()) R.style.MyDatePickerDialogDarkTheme else R.style.MyDatePickerDialogTheme,
        { _, selectedHour: Int, selectedMinute: Int ->
            toTime = LocalTime.of(selectedHour, selectedMinute)
            toDateTime = LocalDateTime.of(toDate, toTime)
            getDifference(fromDateTime, toDateTime)
            calendarTo.set(yearTo, monthTo, dayOfMonthTo,selectedHour,selectedMinute)
        }, hourTo, minuteTo, true
    )


    val seconds: Long = (timeDifference / 1000L)
    val minutes: Long = (timeDifference / 1000L / 60L)
    val hours: Long = (timeDifference / 1000L / 60L / 60L)
    val days: Long = (timeDifference / 1000L / 60L / 60L / 24L * 100).toInt() / 100L
    val months: Float = (timeDifference / 1000L / 60L / 60L / 24L / settings.days.toFloat() * 100).toInt() / 100F
    val years: Float = (months / 12 * 100).toInt() / 100F

    val title = stringResource(R.string.Time_machine)

    BackHandler(enabled = true, onBack = {
        onEvent(DrawerEvents.OnItemClick(title))
        navController.navigate(Screens.MainScreen.rout) { navController.popBackStack() }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn( modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(10.dp)
            .padding(top = 10.dp),
        ) {
            item {   Spacer(modifier = Modifier.height(10.dp))
                Text(text = stringResource(R.string.FROM), modifier = Modifier.padding(start = 20.dp), fontSize = 18.scaledSp())
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
                            datePickerFrom.show()
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),contentAlignment = Alignment.Center
                    ) {
                        Text(text = fromDate.format(formatDate), fontSize = 16.scaledSp(), color = GreyTextColor)
                    }
//            Spacer(modifier = Modifier.width(20.dp))
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
                            timePickerFrom.show()
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),contentAlignment = Alignment.Center
                    ) {
                        Text(text = fromTime.format(formatTime), fontSize = 16.scaledSp(), color = GreyTextColor)
                    }
//            Spacer(modifier = Modifier.width(20.dp))
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
                            getDifference(fromDateTime, toDateTime)
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.NOW),
                            fontSize = 16.scaledSp(),
                            color = GreyTextColor
                        )
                    }
                } }
            item {  Spacer(modifier = Modifier.height(20.dp))
                Text(text = stringResource(R.string.TO), modifier = Modifier.padding(start = 20.dp), fontSize = 18.scaledSp(),)
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
                            datePickerTo.show()
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),contentAlignment = Alignment.Center
                    ) {
                        Text(text = toDate.format(formatDate), fontSize = 16.scaledSp(), color = GreyTextColor)
                    }
//            Spacer(modifier = Modifier.width(20.dp))
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
                            timePickerTo.show()
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),contentAlignment = Alignment.Center
                    ) {
                        Text(text = toTime.format(formatTime), fontSize = 16.scaledSp(), color = GreyTextColor)
                    }
//            Spacer(modifier = Modifier.width(20.dp))
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
                            calendarTo.set(year, month, dayOfMonth)
                            toDate = LocalDate.of(year, month + 1, dayOfMonth)
                            toTime = LocalTime.now()
                            toDateTime = LocalDateTime.of(toDate, toTime)
                            getDifference(fromDateTime, toDateTime)
                        }
                        .padding(vertical = 18.dp, horizontal = 8.dp),contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.NOW), fontSize = 16.scaledSp(), color = GreyTextColor)
                    }
                } }
            item {  Spacer(modifier = Modifier.height(30.dp))
                Row() {
                    Text(
                        text = stringResource(id = R.string.DIFFERENCE) +
                                " :\n $hours " + stringResource(R.string.hours) +
                                "\n= $years " + stringResource(R.string.years) +
                                "\n= $months " + stringResource(R.string.months) +
                                "\n= $days " + stringResource(R.string.days) +
                                "\n= $minutes " + stringResource(R.string.minutes) +
                                "\n= $seconds " + stringResource(R.string.seconds),
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
                } }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.heightIn(min = 60.dp), contentAlignment = Alignment.Center) {
            BannerAdView(id = stringResource(id = R.string.main_banner3))
        }
    }
}
