package com.example.trackingui.components.dialogs

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivityDialog(
    onEdit: (id: String, listIndex: Int, activity: ActivityCategory, count: Int) -> Unit,
    id: MutableState<ModeldataId>,
    state: MutableState<Boolean>,
    activityEnumClass: ActivityCategory,
    listIndex: Int,
    oldCount : Int,
) {

    val activity: MutableState<ActivityCategory> = remember { mutableStateOf(activityEnumClass) }
    var countState by remember {
        mutableStateOf(oldCount)
    }
    var expanded by remember { mutableStateOf(false) }

    CommonDialog(state) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit this activity",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
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
                        modifier = Modifier.menuAnchor(),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = LightBlue,
                            unfocusedIndicatorColor = LightBlue
                        )
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
                value = countState.toString(), onValueChange = {
                    countState = it.toIntOrNull() ?: 0
                },
                label = {
                    Text(text = "Number of Cycles")
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onEdit(id.value.index, listIndex, activity.value, countState)
                        state.value = false
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ), shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Edit")
                }

                Text(
                    text = "Cancel",
                    modifier = Modifier.clickable { state.value = false },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

