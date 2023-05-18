package com.example.trackingui.components.buttons

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.trackingui.R
import com.example.trackingui.components.infinitescroll.loadMoreData
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.TimelineEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleHeader(
    dateTime: MutableState<LocalDate>,
    modifier: Modifier = Modifier,
    allData: List<TimelineEvent>,
    listState: LazyListState,
    scope: CoroutineScope,
    updateCurrentDate: (LocalDate) -> Unit,
    findTargetIndex: (List<TimelineEvent>, LocalDate) -> Int,
) {
    val isNext = remember {
        mutableStateOf(true)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        IconButton(modifier = modifier.then(modifier.size(60.dp)),
            onClick = {
                isNext.value = false
                val newDate = dateTime.value.minusYears(1)
                if (newDate >= allData.lastOrNull()?.hours?.toLocalDate()) {
                    updateCurrentDate(newDate)
                }

            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                contentDescription = "skip",
                tint = Color.Unspecified
            )
        }
        IconButton(modifier = modifier.then(modifier.size(60.dp)),
            onClick = {
                isNext.value = false
                val newDate = dateTime.value.minusDays(1)
                if (newDate >= allData.lastOrNull()?.hours?.toLocalDate()) {
                    updateCurrentDate(newDate)
                    val targetIndex = findTargetIndex(allData, newDate)
                    scope.launch {
                        if (targetIndex != -1) {
                            listState.scrollToItem(targetIndex)
                        }
                    }
                }
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
            targetState = getTitleText(dateTime.value),
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
                isNext.value = true
                val newDate = dateTime.value.plusDays(1)
                if (newDate <= allData.firstOrNull()?.hours?.toLocalDate()) {
                    updateCurrentDate(newDate)
                    val targetIndex = findTargetIndex(allData, newDate)
                    scope.launch {
                        if (targetIndex != -1) {
                            listState.scrollToItem(targetIndex)
                        }
                    }
                }
            }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "skip",
                tint = Color.Unspecified,
            )
        }
        IconButton(modifier = modifier.then(modifier.size(60.dp)),
            onClick = {
                isNext.value = true
                val newDate = dateTime.value.plusYears(1)
                if (newDate <= allData.firstOrNull()?.hours?.toLocalDate()) {
                    updateCurrentDate(newDate)
                }
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
    val dayOfWeek = DateTimeFormatter.ofPattern("     " + "EEEE")
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    return "${dayOfWeek.format(day)}\n${dateFormatter.format(day)}"
}

