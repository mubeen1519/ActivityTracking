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
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.FitnessActivityType
import com.example.trackingui.model.MeditationActivityType
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.ui.theme.LightBlue
import com.example.trackingui.ui.theme.LightOrange

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityDialog(
    onAddEvent: (id: String, maxCount : Int ,activity : ActivityCategory,listIndex : Int) -> Unit,
    id: MutableState<ModeldataId>,
    state: MutableState<Boolean>,
    activityEnumClass: ActivityCategory,
    listIndex : Int
) {
    var activity : MutableState<ActivityCategory> =  remember { mutableStateOf(activityEnumClass) }
    var maxCount by remember {
        mutableStateOf(0)
    }


    var expanded by remember { mutableStateOf(false) }

    CommonDialog(state) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White),
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
                        value = activity.value.getName(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        when (activityEnumClass) {
                            is ActivityCategory.FitnessActivity -> {
                                for (activityType in FitnessActivityType.values()) {
                                    DropdownMenuItem(
                                        text = { Text(text = activityType.name) },
                                        onClick = {
                                            activity.value =
                                                ActivityCategory.FitnessActivity(activityType)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                            is ActivityCategory.MeditationActivity -> {
                                for (activityType in MeditationActivityType.values()) {
                                    DropdownMenuItem(
                                        text = { Text(text = activityType.name) },
                                        onClick = {
                                            activity.value =
                                                ActivityCategory.MeditationActivity(activityType)
                                            expanded = false
                                        }
                                    )
                                }
                            }
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
                            onAddEvent(id.value.index, maxCount, activity.value, listIndex)
                            state.value = false
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = LightBlue,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ), shape = RoundedCornerShape(5.dp)
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