package com.example.trackingui.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.trackingui.ui.theme.Orange
import com.example.trackingui.ui.theme.ParrotGreen
import com.example.trackingui.ui.theme.SkyBlue
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
data class TimelineEvent(
    val id: String = UUID.randomUUID().toString(),
    var hours: LocalDateTime = LocalDateTime.now(),
    var metricPercentage: Double = 0.0,
    var list: MutableList<ActivityTypeAndCount> = mutableListOf()
)

data class ActivityTypeAndCount(
    var count: Int = 0,
    var activity: ActivityCategory
)

data class ModeldataId(
    val index: String,
)

enum class FitnessActivityType(var color: Color) {
    Running(SkyBlue),
    Walking(Orange),
    Stepper(ParrotGreen),
    Elliptical(Color.Red)
}


enum class MeditationActivityType(var color : Color){
    Prayer(SkyBlue),
    Yoga(Orange),
    Pilates(ParrotGreen),
    BodyBalance(Color.Red)
}

sealed class ActivityCategory(val color : Color) {
    class FitnessActivity(val type: FitnessActivityType) : ActivityCategory(type.color){
        override fun getName() = type.name
    }
    class MeditationActivity(val type: MeditationActivityType) : ActivityCategory(type.color){
        override fun getName() = type.name

    }
    abstract fun getName() :String
}

