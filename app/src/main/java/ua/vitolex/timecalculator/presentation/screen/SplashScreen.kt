package ua.vitolex.timecalculator.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import ua.vitolex.timecalculator.utils.DrawerEvents
import java.util.Calendar
import java.util.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplashScreen(navController: NavController, onEvent: (DrawerEvents) -> Unit) {
    val alpha1 = remember {
        Animatable(0f)
    }
    val alpha2 = remember {
        Animatable(1f)
    }
    val size = remember {
        Animatable(0.5f)
    }


    var currentTimeInMs by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(50)
            currentTimeInMs = currentTimeInMs- 10000
        }
    }

    LaunchedEffect(key1 = true) {


        delay(1000L)
        alpha2.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 500,
            )
        )
        delay(10L)
        size.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(
                durationMillis = 500,
            )
        )
        alpha1.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
            )
        )


        alpha1.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 200,
            )
        )
        delay(500L)
        navController.popBackStack()
        navController.navigate(Screens.MainScreen.rout)
        onEvent(DrawerEvents.OnItemClick("Time Machine"))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4EBF1)),
        contentAlignment = Alignment.Center
//        verticalArrangement = Arrangement.SpaceAround,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Time Machine",
            fontSize = 42.sp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha1.value)
                .scale(size.value)
            ,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(4.dp))

        Clock(
            modifier = Modifier.fillMaxWidth(0.5f).alpha(alpha2.value),

            time = {
                currentTimeInMs
            },
            circleRadius = 300f,
            outerCircleThickness = 40f,
            color = MaterialTheme.colors.primary,
            secondaryColor = MaterialTheme.colors.onSurface
        )

//        Image(
//            painter = painterResource(id = R.drawable.logo),
//            contentDescription = "logo",
//            modifier = Modifier
//                .height(150.dp)
//                .width(150.dp)
//                .alpha(alpha.value),
//        )
        Spacer(modifier = Modifier.padding(24.dp))
    }
}

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    time: () -> Long,
    circleRadius: Float,
    outerCircleThickness: Float,
    color: Color,
    secondaryColor: Color
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .circleShadow(
                    color = MaterialTheme.colors.onSecondary,
//                    color = Color.Yellow,
                    offsetX = (0).dp,
                    offsetY = (0).dp,
                    blurRadius = 10.dp,
                    radius = circleRadius - 15f
                )
                .circleShadow(
                    color = MaterialTheme.colors.onPrimary,
//                    color = Color.Blue,
                    offsetX = (10).dp,
                    offsetY = (5).dp,
                    blurRadius = 10.dp,
                    radius = circleRadius - 15f
                )
                .circleShadow(
                    color = color,
                    offsetX = (0).dp,
                    offsetY = (0).dp,
                    blurRadius = 20.dp,
                    radius =  circleRadius - 5f
                )

        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)
            val date = Date(time())
            val cal = Calendar.getInstance()
            cal.time = date
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)
            val seconds = cal.get(Calendar.SECOND)

            drawCircle(
                style = Stroke(
                    width = outerCircleThickness
                ),
//                color = color,
                brush = Brush.verticalGradient(
                    listOf(
//                        color,
                        secondaryColor,
//                        color.copy(0.9f),
//                        color.copy(0.75f),
                        color.copy(0.7f),
//                        color,
                    ),
                ),
                radius = circleRadius + outerCircleThickness / 2f,
                center = circleCenter,
            )

            drawCircle(
                color = GreyTextColor,
                radius = 15f,
                center = circleCenter
            )

            val littleLineLength = circleRadius * 0.1f
            val largeLineLength = circleRadius * 0.2f

            for (i in 0 until 60) {
                val angleInDegrees = i * 360f / 60
                val angleInRad = angleInDegrees * PI / 180f + PI / 2f
                val lineLength = if (i % 5 == 0) largeLineLength else littleLineLength
                val lineThickness = if (i % 5 == 0) 4f else 2f

                val start = Offset(
                    x = (circleRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                    y = (circleRadius * sin(angleInRad) + circleCenter.y).toFloat()
                )
                val end = Offset(
                    x = (circleRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                    y = (circleRadius * sin(angleInRad) + lineLength + circleCenter.y).toFloat()
                )
                rotate(
                    angleInDegrees + 180,
                    pivot = start
                ) {
                    drawLine(
                        color = GreyTextColor,
                        start = start,
                        end = end,
                        strokeWidth = lineThickness.dp.toPx()
                    )
                }
            }
            val clockHands = listOf(ClockHand.Seconds, ClockHand.Minutes, ClockHand.Hours)

            clockHands.forEach { clockHand ->
                val angleInDegrees = when (clockHand) {
                    ClockHand.Seconds -> {
                        seconds * 360f / 60f
                    }
                    ClockHand.Minutes -> {
                        (minutes + seconds / 60f) * 360f / 60f
                    }
                    ClockHand.Hours -> {
                        (((hours % 12) / 12f * 60f) + minutes / 12f) * 360f / 60f
                    }
                }
                val lineLength = when (clockHand) {
                    ClockHand.Seconds -> {
                        circleRadius * 0.8f
                    }
                    ClockHand.Minutes -> {
                        circleRadius * 0.7f
                    }
                    ClockHand.Hours -> {
                        circleRadius * 0.5f
                    }
                }
                val lineThickness = when (clockHand) {
                    ClockHand.Seconds -> {
                        3f
                    }
                    ClockHand.Minutes -> {
                        5f
                    }
                    ClockHand.Hours -> {
                        7f
                    }
                }
                val start = Offset(
                    x = circleCenter.x,
                    y = circleCenter.y
                )
                val end = Offset(
                    x = circleCenter.x,
                    y = circleCenter.y + lineLength
                )
                rotate(
                    angleInDegrees - 180,
                    pivot = start
                ) {
                    drawLine(
                        color = if (clockHand == ClockHand.Seconds) Color.Red.copy(0.7f) else GreyTextColor,
                        start = start,
                        end = end,
                        strokeWidth = lineThickness.dp.toPx()
                    )
                }
            }

        }
    }
}

enum class ClockHand {
    Seconds,
    Minutes,
    Hours
}
