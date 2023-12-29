package ua.vitolex.timecalculator.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.ui.theme.GreyTextColor
import ua.vitolex.timecalculator.ui.theme.exo

@Composable
fun AppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = title, fontSize = 22.scaledSp(), fontFamily = exo)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer",
                    tint = GreyTextColor
                )
            }
        }
    )
}