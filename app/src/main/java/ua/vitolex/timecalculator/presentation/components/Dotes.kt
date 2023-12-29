package ua.vitolex.timecalculator.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Dotes(color: Color) {
    Column (modifier = Modifier.fillMaxHeight().padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .background(color, CircleShape)
            .size(3.dp) )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier
            .background(color, CircleShape)
            .size(3.dp) )
    }
}