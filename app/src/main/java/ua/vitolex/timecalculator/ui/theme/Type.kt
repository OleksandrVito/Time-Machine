package ua.vitolex.timecalculator.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ua.vitolex.timecalculator.R

val campton = FontFamily(
    Font(
        R.font.campton_book,
        weight = FontWeight.Normal
    ),
)

val exo = FontFamily(
    Font(
        R.font.exo2_light,
        weight = FontWeight.Light
    ),
    Font(
        R.font.exo2_medium,
        weight = FontWeight.Medium
    ),
    Font(
        R.font.exo2_regular,
        weight = FontWeight.Normal
    ),
)
val cairo = FontFamily(
    Font(
        R.font.cairo_medium,
        weight = FontWeight.Medium
    ),
    Font(
        R.font.cairo_bold,
        weight = FontWeight.Bold
    ),

    )

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = exo,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = GreyTextColor
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)