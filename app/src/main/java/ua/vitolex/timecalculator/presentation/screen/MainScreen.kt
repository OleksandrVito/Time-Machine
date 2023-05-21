package ua.vitolex.timecalculator.presentation.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.components.Clock
import ua.vitolex.timecalculator.scaledSp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {
    var currentTimeInMs by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(200)
            currentTimeInMs = System.currentTimeMillis()
        }
    }

    val formatDate = SimpleDateFormat("EEEE, dd LLL, yyyy")
    val currentDate = formatDate.format(Date())

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10))
                        .circleShadow(
                            color = MaterialTheme.colors.onPrimary,
//                    color = Color.Blue,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 20.dp,
                            radius = 305f
                        )
                        .circleShadow(
                            color = MaterialTheme.colors.onSecondary,
//                    color = Color.Green,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp,
                            radius = 335f
                        )
                        .height(270.dp)
                        .width(270.dp), contentAlignment = Alignment.Center
                ) {
                    Clock(
                        modifier = Modifier.fillMaxWidth(),

                        time = {
                            currentTimeInMs
                        },
                        circleRadius = 300f,
                        outerCircleThickness = 40f,
                        color = MaterialTheme.colors.primary,
                        secondaryColor = MaterialTheme.colors.onSurface
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = currentDate,
                    fontSize = 30.scaledSp(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
        BannerAdView(id = stringResource(id = R.string.main_banner))
    }
}