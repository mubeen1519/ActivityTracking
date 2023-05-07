package com.example.trackingui.components.infinitescroll

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.trackingui.screens.ActivityType
import com.example.trackingui.screens.ActivityTypeAndCount
import com.example.trackingui.screens.TimelineEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun loadMoreData(date: LocalDate): List<TimelineEvent> {
    val newData = mutableListOf<TimelineEvent>()
    for (i in 1..5) {
        val hour = kotlin.random.Random.nextInt(0,24)
        val minute = kotlin.random.Random.nextInt(0, 60)
        val activity = ActivityType.values().random()
        val maxCount = (0..10).random()
        val hours = LocalDateTime.of(date.minusDays(1), LocalTime.of(hour,minute))
        val progress = 100.0

        val list : MutableList<ActivityTypeAndCount> = mutableListOf()

        repeat(2) {
           list.add(
               ActivityTypeAndCount(
                   activity = ActivityType.values().random(),
                   count = (0..10).random()
               )
           )
        }

        newData.add(TimelineEvent(activity, maxCount, hours,progress,list))
    }
    return newData.sortedBy { it.hours }
}

