package ua.vitolex.timecalculator.presentation.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


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
