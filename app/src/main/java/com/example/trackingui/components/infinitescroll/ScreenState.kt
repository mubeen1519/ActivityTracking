package com.example.trackingui.components.infinitescroll

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.TimelineEvent
import com.example.trackingui.screens.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
fun <T : Enum<T>>loadMoreData(date: LocalDate, enumClass : Class<T>): List<TimelineEvent<T>> {
    val newData = mutableListOf<TimelineEvent<T>>()
    for (i in 1..5) {
        val list : MutableList<ActivityTypeAndCount<T>> = mutableListOf()
        val hour = kotlin.random.Random.nextInt(0,24)
        val minute = kotlin.random.Random.nextInt(0, 60)
        val activity = enumClass.enumConstants!!.random()
        val maxCount = (0..10).random()
        val hours = LocalDateTime.of(date.minusDays(1), LocalTime.of(hour,minute))
        val progress = 100.0
        val id : String = UUID.randomUUID().toString()

        repeat(2) {
           list.add(
               ActivityTypeAndCount(
                   activity = enumClass.enumConstants!!.random(),
                   count = (0..10).random()
               )
           )
        }

        newData.add(TimelineEvent(id,hours,progress,list))
    }
    return newData.sortedBy { it.hours }
}

