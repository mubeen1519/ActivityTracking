package com.example.trackingui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import com.example.trackingui.components.infinitescroll.loadMoreData
import com.example.trackingui.components.timeline.TimeLine
import com.example.trackingui.model.MeditationActivityType
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeditationScreen(navController: NavController) {
    var mDisplayMenu by remember { mutableStateOf(false) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
            Text(
                text = stringResource(id = R.string.meditation_screen),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            IconButton(onClick = { mDisplayMenu = !mDisplayMenu },modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")

                DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }
                ) {
                    DropdownMenuItem(text = { Text(text = "Change Theme") },
                        onClick = { /*TODO*/ }
                    )
                    DropdownMenuItem(text = { Text(text = "Go To Fitness") },
                        onClick = {
                            navController.navigate(CategoryScreens.FitnessScreen.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
        }
        TimeLine<MeditationActivityType>(
            items = loadMoreData(LocalDate.now(), MeditationActivityType::class.java),
            header = { events ->

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    events.list.forEach { value ->
                        Column(
                            Modifier.padding(vertical = 4.dp),
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
            enumClass = MeditationActivityType::class.java
        )
    }
}
