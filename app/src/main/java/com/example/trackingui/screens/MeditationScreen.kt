package com.example.trackingui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackingui.R
import com.example.trackingui.components.timeline.TimeLine
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.MeditationActivityType
import com.example.trackingui.model.TimelineEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeditationScreen(navController: NavController) {
    var mDisplayMenu by remember { mutableStateOf(false) }
    val list: MutableList<ActivityTypeAndCount> = mutableListOf()
    val selectedIndex = remember {
        mutableStateOf(0)
    }
    var tapped by remember { mutableStateOf(false) }
    var showToolTip by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    repeat(2) {
        list.add(
            ActivityTypeAndCount(
                activity = ActivityCategory.MeditationActivity(MeditationActivityType.values().random()),
                count = (0..10).random()
            )
        )
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.meditation_screen),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp)
        )

        IconButton(
            onClick = { mDisplayMenu = !mDisplayMenu },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")

            DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }
            ) {
                DropdownMenuItem(text = { Text(text = "Go To Fitness") },
                    onClick = {
                        navController.navigate(CategoryScreens.FitnessScreen.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        TimeLine(
            header = { events,onItemClick ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    events.list.forEachIndexed { index, value ->
                        Column(
                            Modifier.padding(vertical = 4.dp).clickable {
                                selectedIndex.value = index
                                onItemClick(index)
                                if (selectedIndex.value == index) {
                                    tapped = true
                                    showToolTip =  true
                                    scope.launch {
                                        delay(2000)
                                        showToolTip =  false
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
            activityCategory = ActivityCategory.MeditationActivity(MeditationActivityType.values().random()),
        )
    }
}
