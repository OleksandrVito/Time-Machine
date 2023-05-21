package ua.vitolex.timecalculator.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ua.vitolex.timecalculator.presentation.navigation.Screens
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.utils.DrawerEvents

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplashScreen(navController: NavController, onEvent: (DrawerEvents) -> Unit) {
    val alpha = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
            )
        )
        delay(500L)
        navController.popBackStack()
        navController.navigate(Screens.MainScreen.rout)
        onEvent(DrawerEvents.OnItemClick("Time Machine"))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4EBF1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Time Machine",
            fontSize = 42.sp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha.value),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .height(150.dp)
                .width(150.dp)
                .alpha(alpha.value),
        )
        Spacer(modifier = Modifier.padding(24.dp))
    }
}