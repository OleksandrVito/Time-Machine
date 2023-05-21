package ua.vitolex.timecalculator.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.shadow
import ua.vitolex.timecalculator.shadowAround
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import ua.vitolex.timecalculator.ui.theme.LightBackground
import ua.vitolex.timecalculator.ui.theme.LightShadow1
import ua.vitolex.timecalculator.ui.theme.LightShadow2

@Composable
fun CustomInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
) {
    Box(
        Modifier
            .border(22.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(34.dp))
            .background(color = MaterialTheme.colors.primary)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(0.dp))
    ) {
        Box(
            modifier = Modifier
                .height(70.dp)
                .padding(0.dp)
                .shadowAround(
                    color = MaterialTheme.colors.onSecondary,
                    offsetX = (-5).dp,
                    offsetY = (-5).dp,
                    blurRadius = 5.dp
                )
                .shadow(
                    color = MaterialTheme.colors.onPrimary,
                    offsetX = (8).dp,
                    offsetY = (14).dp,
                    blurRadius = 10.dp
                )
                .padding(start = 16.dp, end = 14.dp)
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = if (value !== "0") value else "", onValueChange = onValueChange,
                cursorBrush = Brush.radialGradient(
                    listOf(
                        GreyTextColor,
                        GreyTextColor
                    )
                ),
                textStyle = MaterialTheme.typography.body1.copy(
                    fontSize = 17.scaledSp(),
                    color = GreyTextColor,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .shadowAround(
                        color = MaterialTheme.colors.primary,
                        offsetX = (-8).dp,
                        offsetY = (-8).dp,
                        blurRadius = 8.dp
                    )
                    .background(MaterialTheme.colors.primary)
                    .height(35.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 5.dp, end = 5.dp),
                keyboardOptions = keyboardOptions ?: KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrect = true,
                ),
                keyboardActions = keyboardActions ?: KeyboardActions(

                ),
                decorationBox = { innerTextField ->
                    Box() {
                        if (value.isEmpty()) {
                            Text(
                                text = "0", color = GreyTextColor.copy(0.5f),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}