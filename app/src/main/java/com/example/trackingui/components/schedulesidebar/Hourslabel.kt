package com.example.trackingui.components.schedulesidebar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackingui.ui.theme.TrackingUITheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
private val hourFormatter = DateTimeFormatter.ofPattern("h a")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SidebarTimeLabel(
    time: LocalTime,
    modifier: Modifier = Modifier
) {
    Text(
        text = time.format(hourFormatter), modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun BasicSidebarLabelPreview() {
    TrackingUITheme {
        SidebarTimeLabel(time = LocalTime.NOON, Modifier.sizeIn(maxHeight = 64.dp))
    }
}