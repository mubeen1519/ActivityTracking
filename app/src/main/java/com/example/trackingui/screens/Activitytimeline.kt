package com.example.trackingui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.trackingui.R
import com.example.trackingui.components.infinitescroll.loadMoreData
import com.example.trackingui.components.timeline.*
import com.example.trackingui.ui.theme.Orange
import com.example.trackingui.ui.theme.ParrotGreen
import com.example.trackingui.ui.theme.SkyBlue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityTimeline() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.fitness_screen),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        TimeLine(
            items = loadMoreData(LocalDate.now()),
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
                                    width = (8.dp + 2.dp) * value.count - 7.dp,
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
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
data class TimelineEvent (
    var activity: ActivityType = ActivityType.Fitness,
    var maxCount: Int = 0,
    var hours: LocalDateTime = LocalDateTime.now(),
    var progress: Double = 0.0,
    var list: MutableList<ActivityTypeAndCount> = mutableListOf()
)

data class ActivityTypeAndCount(
    var count: Int,
    var activity: ActivityType = ActivityType.Fitness
)

enum class ActivityType(var color: Color) {
    Mediation(SkyBlue),
    Fitness(Orange),
    Office(ParrotGreen),
    Testing(Color.Red)
}




