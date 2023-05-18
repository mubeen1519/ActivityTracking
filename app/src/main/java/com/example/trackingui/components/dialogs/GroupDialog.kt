package com.example.trackingui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.model.TimelineEvent
import com.example.trackingui.ui.theme.LightBlue

@Composable
fun GroupDialog(
    onGroup: (id: String, listIndex: Int) -> Unit,
    listIndex: Int,
    id: MutableState<ModeldataId>,
    state: MutableState<Boolean>,
    event: TimelineEvent
) {

    CommonDialog(state = state) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Do you want to track all ${event.list[listIndex].activity.getName()} activities?",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onGroup(id.value.index, listIndex)
                        state.value = false
                    }, shape = RoundedCornerShape(5.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Group")
                }

                Button(
                    onClick = { state.value = false },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }

    }

}