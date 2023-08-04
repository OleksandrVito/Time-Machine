package ua.vitolex.timecalculator.presentation

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
//    val context = LocalContext.current
    val calendarFrom: Calendar = Calendar.getInstance()
    val calendarTo: Calendar = Calendar.getInstance()
    val calendarCurrent: Calendar = Calendar.getInstance()

    var focus by
    mutableStateOf(false)

}