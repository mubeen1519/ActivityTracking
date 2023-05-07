package com.example.trackingui.components.timeline

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.trackingui.R
import com.example.trackingui.ui.theme.LightOceanBlue


interface TimeLineOption {
    val circleIcon: Int
    val circleSize: Dp
    val circleColor: Color
    val lineColor: Color
    val lineWidth: Dp
    val contentHeight: Dp
}

class TimeLineOptionImpl(
    override val circleIcon: Int,
    override val circleSize: Dp,
    override val circleColor: Color,
    override val lineColor: Color,
    override val lineWidth: Dp,
    override val contentHeight: Dp,
) : TimeLineOption



fun TimeLineOption(
    @DrawableRes circleIcon: Int = R.drawable.outline_circle_24,
    circleSize: Dp = 30.dp,
    circleColor: Color = Color.Unspecified,
    lineColor: Color = LightOceanBlue,
    lineWidth: Dp = 1.dp,
    contentHeight: Dp = 150.dp,
): TimeLineOption =
    TimeLineOptionImpl(
        circleIcon,
        circleSize,
        circleColor,
        lineColor,
        lineWidth,
        contentHeight,
    )

//data class TimelineOptions(
//    val lineColor: Color = Color.Blue,
//    val lineThickness: Float = 12F,
//    val lineStartMargin: Dp = 48.dp,
//    val lineEndMargin: Dp = 36.dp,
//    val iconSize: Dp = 26.dp,
//    val enableItemAnimation: Boolean = false,
//    val iconShape: Shape = CircleShape,
//    val itemSpacing: Dp = 0.dp,
//    val circleColor: Color = Color.Unspecified,
//    val showIcons: Boolean = true,
//    val lineType: LineType = LineType.Solid,
//
//)
//
//sealed class LineType {
//    object Solid : LineType()
//    class Dashed(
//        val intervals: FloatArray = floatArrayOf(20f, 20f),
//        val phase: Float = 50f
//    ) : LineType()
//}