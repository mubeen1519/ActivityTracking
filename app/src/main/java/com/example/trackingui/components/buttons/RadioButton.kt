package com.example.trackingui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

data class RadioItems(
    var id: Int,
    var name: String,
)

@Composable
fun RadioGroup(
    items: Iterable<RadioItems>,
    selected: Int,
    onItemSelected: ((Int) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .selectableGroup()
        .background(MaterialTheme.colorScheme.secondary)) {
        items.forEach { item ->
            RadioGroupItems(
                items = item,
                selected = selected == item.id,
                onClick = { onItemSelected?.invoke(item.id) },

                )
        }
    }
}

@Composable
private fun RadioGroupItems(
    items: RadioItems,
    selected: Boolean,
    onClick: ((Int) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = { onClick?.invoke(items.id) },
                role = Role.RadioButton
            )
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 8.dp, start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = modifier.width(5.dp))
        Text(
            text = items.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall
        )
    }
}