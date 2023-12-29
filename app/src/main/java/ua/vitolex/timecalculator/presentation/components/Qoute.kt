package ua.vitolex.timecalculator.presentation.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.vitolex.timecalculator.R
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.ui.theme.GreyTextColor

@Composable
fun Qoute(str: String) {
    val myId = "inlineContent"
    val text = buildAnnotatedString {
        appendInlineContent(myId, "[icon]")
        append(str.split("|")[0])
    }
    val context = LocalContext.current
    fun shareTextOrLink(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        try {
            context.startActivity(shareIntent)
        } catch (_: Exception) {
        }
    }

    val inlineContent = mapOf(
        Pair(
            myId,
            InlineTextContent(
                Placeholder(
                    width = 24.sp,
                    height = 23.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_quote),
                    tint = GreyTextColor,
                    contentDescription = "qoute",
                    modifier = Modifier
                        .width(22.dp)
                        .height(24.dp)
                        .padding(bottom = 2.dp),
                )
            }
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        verticalArrangement = Arrangement.Top
    ) {
       
        Text(
            text = text,
            fontSize = 24.scaledSp(),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start,
            inlineContent = inlineContent
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = str.split("|")[1],
            fontSize = 18.scaledSp(),
            modifier = Modifier
                .fillMaxWidth().padding(end = 14.dp, bottom = 0.dp),
            textAlign = TextAlign.Right
        )
        Row(modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = {
                shareTextOrLink(context, "\"${str.split("|")[0]}\" - ${str.split("|")[1]}")
            }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = GreyTextColor
                )
            }
        }
    }

}