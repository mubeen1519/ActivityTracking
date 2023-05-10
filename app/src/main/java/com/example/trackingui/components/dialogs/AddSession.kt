package com.example.trackingui.components.dialogs

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.model.TimelineEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun <T : Enum<T>> AddSession(
    onAddSession: (TimelineEvent<T>, index : String) -> Unit,
    state: MutableState<Boolean>,
    currentDate: LocalDate,
    data : MutableState<ModeldataId>,
    enumClass: Class<T>
) {
        val activityCountList : MutableList<ActivityTypeAndCount<T>> = remember {
        mutableStateListOf(ActivityTypeAndCount(
            activity = enumClass.enumConstants!!.random(),
            count = 8
        ))
    }

    val context = LocalContext.current
    val time = LocalDateTime.of(currentDate, LocalTime.now())
    val timeValue = remember { mutableStateOf("") }


    val hour = time.hour
    val minute = time.minute
    var event by remember {
        mutableStateOf(
            TimelineEvent(
                hours = LocalDateTime.of(currentDate, LocalTime.now()),
                metricPercentage = 70.0,
                list = activityCountList
            )
        )
    }

    fun onTimeChange(hour: Int, minute: Int) {
        event = event.copy(
            hours = LocalDateTime.of(
                currentDate,
                LocalTime.of(hour.toClockPattern().toInt(), minute.toClockPattern().toInt())
            )
        )
        onAddSession(event,data.value.index)
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hours: Int, minutes: Int ->
            onTimeChange(hours, minutes)
            timeValue.value = "$hours:$minutes"
        }, hour, minute, false
    )

    AlertDialog(onDismissRequest = { state.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp).background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = { timePickerDialog.show() }) {
                Text(text = "Select Time")
            }
        }
    }

}


private fun Int.toClockPattern(): String {
    return if (this < 10) "0$this" else "$this"
}
