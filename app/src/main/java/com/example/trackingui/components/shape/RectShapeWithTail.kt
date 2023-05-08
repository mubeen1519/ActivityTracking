package com.example.trackingui.components.shape

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.trackingui.screens.TimelineEvent
import com.example.trackingui.ui.theme.LightOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Bubble(
    modifier: Modifier = Modifier,
    date: LocalDate,
    events : TimelineEvent

) {
    val density = LocalDensity.current
    val arrowHeight = 16.dp

    val bubbleShape = remember {
        getBubbleShape(
            density = density,
            cornerRadius = 8.dp,
            arrowWidth = 20.dp,
            arrowHeight = arrowHeight,
            arrowOffset = 13.dp
        )
    }

    Box(
        modifier = modifier
            .clip(bubbleShape)
            .shadow(2.dp)
            .background(LightOrange)
            .padding(bottom = arrowHeight)
            .width(width = 100.dp),
    ) {
        Box(modifier = Modifier.padding(top = 8.dp, end = 8.dp, start = 8.dp, bottom = 4.dp)) {
            Column {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "${events.progress.toInt()}%",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = events.activity.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }


fun getBubbleShape(
    density: Density,
    cornerRadius: Dp,
    arrowWidth: Dp,
    arrowHeight: Dp,
    arrowOffset: Dp
): GenericShape {

    val cornerRadiusPx: Float
    val arrowWidthPx: Float
    val arrowHeightPx: Float
    val arrowOffsetPx: Float

    with(density) {
        cornerRadiusPx = cornerRadius.toPx()
        arrowWidthPx = arrowWidth.toPx()
        arrowHeightPx = arrowHeight.toPx()
        arrowOffsetPx = arrowOffset.toPx()
    }

    return GenericShape { size: Size, layoutDirection: LayoutDirection ->

        val rectBottom = size.height - arrowHeightPx
        this.addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset.Zero,
                    size = Size(size.width, rectBottom)
                ),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
        )
        moveTo(arrowOffsetPx, rectBottom)
        lineTo(arrowOffsetPx + arrowWidthPx / -2, size.height)
        lineTo(arrowOffsetPx + arrowWidthPx, rectBottom)

    }
}
