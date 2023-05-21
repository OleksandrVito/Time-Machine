package ua.vitolex.timecalculator.presentation.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.vitolex.timecalculator.circleShadow
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.utils.DrawerEvents
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DrawerHeader() {
    var currentTimeInMs by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(200)
            currentTimeInMs = System.currentTimeMillis()
        }
    }
    val formatDate = SimpleDateFormat("EEEE, dd LLLL, yyyy")
    val currentDate = formatDate.format(Date())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                .height(200.dp)
                .width(200.dp), contentAlignment = Alignment.Center
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
        Text(text = "Time Machine", fontSize = 35.scaledSp())
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    navController: NavController,
    closeNavDrawer: () -> Unit,
    onEvent: (DrawerEvents) -> Unit,
) {
    val scope = rememberCoroutineScope()
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(item.route) {
                            navController.popBackStack()
//                            if (item.title !== "Home") popUpTo(Screens.MainScreen.rout){}
                        }
                        scope.launch {
                            delay(400L)
                            closeNavDrawer()
                        }
                        if (item.title == "Home") {
                            onEvent(DrawerEvents.OnItemClick(""))
                        } else {
                            onEvent(DrawerEvents.OnItemClick(item.title))
                        }

                    }) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.body1,
                    fontSize = 22.scaledSp(),
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .padding(start = 40.dp)
                )
            }
        }
    }
}

data class MenuItem(
    val title: String,
    val route: String,
)