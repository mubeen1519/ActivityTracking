package com.example.trackingui.components.buttons

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.trackingui.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleHeader(
    dateTime: LocalDate,
    modifier: Modifier = Modifier,
    onDateSelect: (LocalDate) -> Unit
) {
    val isNext = remember {
        mutableStateOf(true)
    }


    Row(
        modifier = modifier
            .fillMaxWidth().padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
            IconButton(modifier = modifier.then(modifier.size(60.dp)),
                onClick = {
                onDateSelect(dateTime.minusDays(1))
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                    contentDescription = "skip",
                    tint = Color.Unspecified
                )
            }
            IconButton( modifier = modifier.then(modifier.size(60.dp)),
                onClick = {
                onDateSelect(dateTime.minusYears(1))
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "skip",
                    tint = Color.Unspecified
                )
            }

            AnimatedContent(
                modifier = modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically)
                    .zIndex(1f),
                targetState = getTitleText(dateTime),
                transitionSpec = {
                    addAnimation(isNext = isNext.value).using(
                        SizeTransform(clip = false)
                    )
                }
            ) {
                Spacer(modifier = modifier.padding(start = 8.dp))
                Text(text = it, modifier = modifier, color = Color.Gray)
                Spacer(modifier = modifier.padding(end = 8.dp))
            }

            IconButton(modifier = modifier.then(modifier.size(60.dp)),
                onClick = {
                onDateSelect(dateTime.plusDays(1))
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "skip",
                    tint = Color.Unspecified,
                )
            }
            IconButton(modifier = modifier.then(modifier.size(60.dp)),
                onClick = {
                onDateSelect(dateTime.plusYears(1))
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_skip_next_24),
                    contentDescription = "skip",
                    tint = Color.Unspecified
                )
            }
        }
    }



@OptIn(ExperimentalAnimationApi::class)
internal fun addAnimation(duration: Int = 500, isNext: Boolean): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) height else -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) -height else height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTitleText(day: LocalDate): String {
    val dayOfWeek = DateTimeFormatter.ofPattern("    EEEE")
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    return "${dayOfWeek.format(day)}\n${dateFormatter.format(day)}"
}