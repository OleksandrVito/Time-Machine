package ua.vitolex.timecalculator.presentation.screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.presentation.components.BannerAdView
import ua.vitolex.timecalculator.presentation.components.Clock
import ua.vitolex.timecalculator.presentation.components.Qoute
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
    val context = LocalContext.current
    val language = Locale.getDefault().language

    val formatDate = SimpleDateFormat("EEEE, dd LLL, yyyy")
    val currentDate = formatDate.format(Date())

    val qoutesArrey = listOf<String>(
        "Antoine.txt",
        "Bruyere.txt",
        "Bulgakov.txt",
        "Covey.txt",
        "Darwin.txt",
        "Einstein.txt",
        "Franklin.txt",
        "Jobs.txt",
        "Kornfield.txt",
        "Pericles.txt",
        "Rohn.txt",
        "Russell.txt",
        "Schopenhauer.txt",
        "Seneca.txt",
        "Seneca2.txt",
        "Shakespeare.txt",
        "Shakespeare2.txt",
        "Teresa.txt",
        "Theophrastus.txt",
        "Tolstoy.txt",
        "Warhol.txt",
        "Wilde.txt"
    )
    val rundomNum by remember {
        mutableStateOf((0..qoutesArrey.size-1).random())
    }

    val str =
        if (language == "uk") {
        context.assets.open("txtua/${qoutesArrey[rundomNum]}").bufferedReader().use {
            it.readText()
        }
    } else {
        context.assets.open("txten/${qoutesArrey[rundomNum]}").bufferedReader().use {
            it.readText()
        }
    }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
//                Spacer(modifier = Modifier.height(10.dp))
                Qoute(str)
            }
            item {
//                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10))
                        .circleShadow(
                            color = MaterialTheme.colors.onPrimary,
//                    color = Color.Blue,
                            offsetX = (-5).dp,
                            offsetY = (-5).dp,
                            blurRadius = 15.dp,
                            radius = 205f
                        )
                        .circleShadow(
                            color = MaterialTheme.colors.onSecondary,
//                    color = Color.Green,
                            offsetX = (5).dp,
                            offsetY = (5).dp,
                            blurRadius = 10.dp,
                            radius = 215f
                        )
                        .height(250.dp)
                        .width(250.dp), contentAlignment = Alignment.Center
                ) {
                    Clock(
                        modifier = Modifier.fillMaxWidth(),

                        time = {
                            currentTimeInMs
                        },
                        circleRadius = 200f,
                        outerCircleThickness = 30f,
                        color = MaterialTheme.colors.primary,
                        secondaryColor = MaterialTheme.colors.onSurface
                    )
                }
            }
            item {
//                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = currentDate,
                    fontSize = 24.scaledSp(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.heightIn(min = 60.dp), contentAlignment = Alignment.Center) {
            BannerAdView(id = stringResource(id = R.string.main_banner1))
        }
    }
}