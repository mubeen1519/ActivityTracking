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
data class TimelineEvent<T : Enum<T>>(
    val id: String = UUID.randomUUID().toString(),
    var hours: LocalDateTime = LocalDateTime.now(),
    var metricPercentage: Double = 0.0,
    var list: MutableList<ActivityTypeAndCount<T>> = mutableListOf()
)

data class ActivityTypeAndCount<T : Enum<T>>(
    var count: Int = 0,
    var activity: T
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

