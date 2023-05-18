package com.example.trackingui.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.model.TimelineEvent
import com.example.trackingui.ui.theme.LightBlue


@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitDialog(
    state: MutableState<Boolean>,
    onSplit: (id: String, index: Int, splitCount: Int) -> Unit,
    id: MutableState<ModeldataId>,
    event: TimelineEvent,
    index: Int,
) {

    var splitCount by remember {
        mutableStateOf(0)
    }

    CommonDialog(state = state) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Activity has cycles  ${event.list[index].count} of ${event.list[index].activity.getName()}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = splitCount.toString(), onValueChange = {
                    splitCount = it.toIntOrNull() ?: 0
                },
                label = {
                    Text(text = "Number of counts to split")
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = LightBlue,
                    unfocusedIndicatorColor = LightBlue
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onSplit(id.value.index, index, splitCount)
                        state.value = false
                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Split", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { state.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Cancel")
                }
            }

        }
    }
}
