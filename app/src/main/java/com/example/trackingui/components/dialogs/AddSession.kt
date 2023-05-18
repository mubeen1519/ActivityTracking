package com.example.trackingui.components.dialogs

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.model.TimelineEvent
import com.example.trackingui.ui.theme.LightBlue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddSession(
    onAddSession: (TimelineEvent, index: String) -> Unit,
    state: MutableState<Boolean>,
    currentDate: LocalDate,
    data: MutableState<ModeldataId>,
    onDeleteSession: (TimelineEvent, index: String) -> Unit,
    activityCategory: ActivityCategory
) {
    val metricPercentage = 8
    val activityCountList: MutableList<ActivityTypeAndCount> = remember {
        mutableStateListOf(
            ActivityTypeAndCount(
                activity = activityCategory,
                count = 8,
                metricPercentage = (metricPercentage.toDouble() / 10) * 100
            )
        )
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
        onAddSession(event, data.value.index)
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
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Select Time To Add New Session",
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                timePickerDialog.show()
                state.value = false
            }, shape = RoundedCornerShape(5.dp), colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = LightBlue
            )) {
                Text(text = "Select Time")
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Wanna Delete this Session?", color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    onDeleteSession(event, data.value.index)
                    state.value = false
                }, shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Red
                )
            ) {
                Text(text = "Delete")
            }
        }
    }

}


private fun Int.toClockPattern(): String {
    return if (this < 10) "0$this" else "$this"
}
