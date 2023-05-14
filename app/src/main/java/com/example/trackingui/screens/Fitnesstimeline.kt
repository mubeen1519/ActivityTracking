package com.example.trackingui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.trackingui.R
import com.example.trackingui.components.dialogs.ChangeThemeDialog
import com.example.trackingui.components.timeline.*
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.FitnessActivityType
import com.example.trackingui.model.TimelineEvent
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FitnessTimeline(navController: NavController, viewModel: ActivityViewModel = hiltViewModel()) {
    var mDisplayMenu by remember { mutableStateOf(false) }
    val changeThemeDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val selectedIndex = remember {
        mutableStateOf(0)
    }
    var tapped by remember { mutableStateOf(false) }
    var showToolTip by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val hour = kotlin.random.Random.nextInt(0, 24)
    val minute = kotlin.random.Random.nextInt(0, 60)
    val hours = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute))

    val list: MutableList<ActivityTypeAndCount> = mutableListOf()

    repeat(2) {
        list.add(
            ActivityTypeAndCount(
                activity = ActivityCategory.FitnessActivity(FitnessActivityType.values().random()),
                count = (0..10).random()
            )
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        if (changeThemeDialog.value) {
            ChangeThemeDialog(state = changeThemeDialog, userSetting = viewModel.themeSetting)
        }
        Text(
            text = stringResource(id = R.string.fitness_screen),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp)
        )
        IconButton(
            onClick = { mDisplayMenu = !mDisplayMenu },
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")
            DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }
            ) {
                DropdownMenuItem(text = { Text(text = "Change Theme") },
                    onClick = { changeThemeDialog.value = true }
                )
                DropdownMenuItem(text = { Text(text = "Go To Meditation") },
                    onClick = {
                        navController.navigate(CategoryScreens.MeditationScreen.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }


        }
        TimeLine(
//            items = listOf(
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                ),
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                ),
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                ),
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                ),
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                ),
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                ),
//                TimelineEvent(
//                    list = list,
//                    hours = hours
//                )
//            ),
            header = { events,onItemClick ->

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    events.list.forEachIndexed { index, value ->
                        Column(
                            Modifier
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedIndex.value = index
                                    onItemClick(index)
                                    if (selectedIndex.value == index) {
                                        tapped = true
                                        scope.launch {
                                            tapped = false
                                        }
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Canvas(
                                Modifier.size(
                                    width = (10.dp + 2.dp) * value.count - 7.dp,
                                    height = 30.dp
                                )
                            ) {

                                val rectangleWidth =
                                    (size.width - ((value.count - 1) * 5.dp.toPx())) / value.count.toFloat()
                                repeat(value.count) { i ->
                                    val rectangleLeft = rectangleWidth * i + (2.dp.toPx() * i)
                                    drawRect(
                                        color = value.activity.color,
                                        topLeft = Offset(rectangleLeft, 0f),
                                        size = Size(rectangleWidth, size.height),
                                        style = Fill
                                    )
                                }
                            }
                        }
                    }
                }
            },
            activityCategory = ActivityCategory.FitnessActivity(FitnessActivityType.values().random()),

            )

    }
}





