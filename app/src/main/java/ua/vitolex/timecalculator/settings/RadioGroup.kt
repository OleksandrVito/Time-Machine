package ua.vitolex.timecalculator.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.vitolex.timecalculator.scaledSp
import ua.vitolex.timecalculator.ui.theme.GreyTextColor

data class RadioButtonItem(
    val id: Int,
    val title: String,
)

@Composable
fun RadioGroup(
    items: Iterable<RadioButtonItem>,
    selected: Int,
    onItemSelect: ((Int) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.selectableGroup()
    ) {
        items.forEach { item ->
            RadioGroupItem(
                item = item,
                selected = selected == item.id,
                onClick = { onItemSelect?.invoke(item.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RadioGroupItem(
    item: RadioButtonItem,
    selected: Boolean,
    onClick: ((Int) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = { onClick?.invoke(item.id) },
                role = Role.RadioButton
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        Text(
            text = item.title,
            fontSize = 16.scaledSp(),
            textAlign = TextAlign.Center,
            modifier = Modifier
//                .padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                unselectedColor = GreyTextColor,
            )
        )
    }
}


private val radioItems = listOf(
    RadioButtonItem(
        id = 1,
        title = "Light Theme",
    ),
    RadioButtonItem(
        id = 2,
        title = "Dark Theme",
    ),
    RadioButtonItem(
        id = 3,
        title = "Auto Theme",
    ),
)