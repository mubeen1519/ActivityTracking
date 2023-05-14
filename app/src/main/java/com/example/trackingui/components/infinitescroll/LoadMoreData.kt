package com.example.trackingui.components.infinitescroll

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.trackingui.model.*
import com.example.trackingui.screens.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
fun loadMoreData(date: LocalDate, activityCategory : ActivityCategory): List<TimelineEvent> {
    val newData = mutableListOf<TimelineEvent>()
    for (i in 1..5) {
        val count = (0..10).random()
        val list: MutableList<ActivityTypeAndCount> = mutableListOf()
        val hour = kotlin.random.Random.nextInt(0, 24)
        val minute = kotlin.random.Random.nextInt(0, 60)
        val hours = LocalDateTime.of(date.minusDays(1), LocalTime.of(hour, minute))
        val progress = (count.toDouble() / 10) * 100
        val id: String = UUID.randomUUID().toString()

        repeat(2) {
            val randomMeditationActivityType = MeditationActivityType.values().random()
            val randomFitnessActivityType = FitnessActivityType.values().random()

            list.add(
                ActivityTypeAndCount(
                    count = count,
                    activity = when(activityCategory){
                        is ActivityCategory.FitnessActivity -> ActivityCategory.FitnessActivity(randomFitnessActivityType)
                        is ActivityCategory.MeditationActivity -> ActivityCategory.MeditationActivity(randomMeditationActivityType)
                    }
                )
            )
        }

        newData.add(TimelineEvent(id, hours, progress, list))
    }
    return newData.sortedBy { it.hours }
}

