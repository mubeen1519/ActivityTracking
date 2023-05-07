package com.example.trackingui.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trackingui.components.timeline.Modeldata
import com.example.trackingui.screens.ActivityType
import com.example.trackingui.screens.ActivityTypeAndCount
import com.example.trackingui.screens.TimelineEvent
import com.example.trackingui.ui.theme.LightOrange
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityDialog(
    onAddEvent: (TimelineEvent, index: Int) -> Unit,
    data: MutableState<Modeldata>,
    state: MutableState<Boolean>
) {
    var activity by remember { mutableStateOf(ActivityType.Fitness) }
    var maxCount by remember {
        mutableStateOf(0)
    }
//    var activityCountList : MutableList<ActivityTypeAndCount> = remember {
//        mutableStateListOf(ActivityTypeAndCount(
//            activity = ActivityType.Fitness,
//            count = 0
//        ))
//    }
    var hours by remember { mutableStateOf(LocalDateTime.now()) }

    var expanded by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { state.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(LightOrange),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add New Event",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = activity.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        for (activityType in ActivityType.values()) {
                            DropdownMenuItem(
                                text = { Text(text = activityType.name) },
                                onClick = {
                                    activity = activityType
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = maxCount.toString(), onValueChange = {
                    maxCount = it.toIntOrNull() ?: 0
                },
                label = {
                    Text(text = "Number of Cycles")
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val event = TimelineEvent(
                            activity = activity,
                            hours = hours,
                            maxCount = maxCount,
                            progress = (maxCount.toDouble() / 10) * 100,
                        )
                        onAddEvent(event, data.value.index)
                        state.value = false
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = LightOrange,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Add")
                }

                Text(
                    text = "Cancel",
                    modifier = Modifier.clickable { state.value = false },
                    color = Color.Cyan
                )
            }
        }
    }
}