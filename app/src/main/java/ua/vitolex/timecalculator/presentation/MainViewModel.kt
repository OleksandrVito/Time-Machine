package ua.vitolex.timecalculator.presentation

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.sql.Time
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
//    val context = LocalContext.current
    val calendarFrom: Calendar = Calendar.getInstance()
    val calendarTo: Calendar = Calendar.getInstance()
    val calendarCurrent: Calendar = Calendar.getInstance()

    var focus by
    mutableStateOf(false)

    data class Time(val days: Int, val hour: Int, val minute: Int, val second: Int)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDifference(
        currentDateTime: LocalDateTime,
        futureDateTime: LocalDateTime,
    ): Time {
        val difference = Duration.between(currentDateTime, futureDateTime).toMillis()
        val seconds = (difference / 1000) % 60
        val minutes = (difference / (1000 * 60)) % 60
        val hours = (difference / (1000 * 60 * 60)) % 24
        val days = (difference / (1000 * 60 * 60 * 24))


        return Time(days.toInt(), hours.toInt(), minutes.toInt(), seconds.toInt())
    }

}