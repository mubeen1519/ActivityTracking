package com.example.trackingui.components.schedulesidebar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleTimeSideBar(
    hoursHeight : Dp,
    modifier: Modifier = Modifier,
    label : @Composable (time : LocalTime) -> Unit = { SidebarTimeLabel(time = it)}
){
    Column(modifier = modifier) {
        val startTime = LocalTime.MIN
        repeat(24){i ->
            Box(modifier = modifier.height(hoursHeight)){
                label(startTime.plusHours(i.toLong()))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ScheduleSidebarPreview() {
    ScheduleTimeSideBar(hoursHeight = 64.dp)
}