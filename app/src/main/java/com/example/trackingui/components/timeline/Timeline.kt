package com.example.trackingui.components.timeline

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeastWrapContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackingui.R
import com.example.trackingui.components.buttons.ScheduleHeader
import com.example.trackingui.components.dialogs.AddActivityDialog
import com.example.trackingui.components.dialogs.AddSession
import com.example.trackingui.components.infinitescroll.loadMoreData
import com.example.trackingui.components.shape.Bubble
import com.example.trackingui.screens.ActivityTypeAndCount
import com.example.trackingui.screens.ActivityViewModel
import com.example.trackingui.screens.TimelineEvent
import com.example.trackingui.ui.theme.LightBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.streams.toList

private const val HeaderIndex = -1

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeLine(
    items: List<TimelineEvent>,
    modifier: Modifier = Modifier,
    timelineOption: TimeLineOption = TimeLineOption(),
    timelinePadding: TimeLinePadding = TimeLinePadding(),
    header: @Composable (TimelineEvent) -> Unit,
) {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    val scope = rememberCoroutineScope()
    var allData by remember { mutableStateOf(items) }

    LaunchedEffect(Unit) { currentDate = allData[0].hours.toLocalDate() }

    val listState = rememberLazyListState()



    val loadData: () -> Unit = {
        val newData =
            if (allData.isEmpty()) loadMoreData(LocalDate.now()) else loadMoreData(allData[allData.size - 1].hours.toLocalDate())
        allData = allData + newData
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ScheduleHeader(dateTime = currentDate) {
                currentDate = it
            }
        }

        LazyColumn(
            state = listState,
            modifier = modifier,
            userScrollEnabled = true,
            contentPadding = timelinePadding.defaultPadding,
        ) {
            val sortedData =
                allData.stream().sorted(Comparator.comparing(TimelineEvent::hours, reverseOrder()))
                    .toList()


            sortedData.forEachIndexed { index, value ->
                stickyHeader {
                    TimelineView(
                        item = value,
                        groupSize = sortedData.size,
                        groupIndex = index,
                        elementsSize = sortedData.size,
                        elementsIndex = HeaderIndex,
                        timelineOption = timelineOption,
                        timelinePadding = timelinePadding,
                        isHeader = true,
                        header = header,
                        hourLabel = value.hours,
                        onAddEvent = { event, i ->
                            println("hello buddy: $allData")
                            val newEvent = allData.toMutableList()
                            val eventIndex = newEvent[i]
                            val newList = eventIndex.list.find { it.activity == event.activity }
                            if (newList != null) {
                                newList.count += event.maxCount
                            } else {
                                eventIndex.list.add(
                                    ActivityTypeAndCount(
                                        count = event.maxCount,
                                        activity = event.activity
                                    )
                                )
                            }
                            allData = newEvent

                        },
                        addSession = { timelineEvent, index1 ->
////                            val session = allData.toMutableList()
////                            val newIndex = session[index1]
////                            val i = session.indexOf(newIndex)
////                            val new = i + 1
////                            session.add(new, timelineEvent)
////                            allData = session
                        },
                        listState = listState
                    )
//                    if (textState == index) {
//                        Text(
//                            text = value.hours.format(
//                                DateTimeFormatter.ofPattern("EEEE")
//                            ),
//                            modifier.padding(end = 6.dp),
//                            color = LightBlue,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
                }


//            itemsIndexed(sortedData) { index, value ->
//                Text("hehe: $index: $value")
//            }
//        }
            }
        }

                listState.ScrolledToEnd(
                    loadMore = {
                        loadData()
                    },
                    setHeaderDate = {
                        scope.launch {
                            currentDate = allData[it].copy().hours.toLocalDate()
                        }
                    }
                )
            }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun LazyListState.ScrolledToEnd(
    loadMore: () -> Unit,
    setHeaderDate: (Int) -> Unit,
    buffer: Int = 2,
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = (layoutInfo.visibleItemsInfo.lastOrNull()
                ?.index ?: 0) + 1
            setHeaderDate(lastVisibleItem)
            lastVisibleItem > (totalItems - buffer)
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) {
                    loadMore()

                }
            }
    }
}

data class Modeldata(
    val index: Int,
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TimelineView(
    item: TimelineEvent,
    hourLabel: LocalDateTime,
    groupSize: Int,
    groupIndex: Int,
    elementsSize: Int,
    elementsIndex: Int,
    listState: LazyListState,
    timelineOption: TimeLineOption,
    timelinePadding: TimeLinePadding,
    isHeader: Boolean,
    header: @Composable (TimelineEvent) -> Unit,
    onAddEvent: (TimelineEvent, index: Int) -> Unit,
    addSession: (TimelineEvent, index: Int) -> Unit
) {
    var tapped by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var showToolTip by remember {
        mutableStateOf(false)
    }
    val textState by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    val addDialogIndex: MutableState<Modeldata> = remember {
        mutableStateOf(Modeldata(index = groupIndex))
    }
    val addDialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val addSessionDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val popupVisible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(timelineOption.contentHeight)
    )
    {
        val (circle, circleInnerLine, topLine, bottomLine, timelineContent, hours, bubbleC,day) = createRefs()
        if (showToolTip) {
            Bubble(
                date = hourLabel.toLocalDate(),
                events = item,
                modifier = Modifier.constrainAs(bubbleC) {
                    start.linkTo(circle.end)
                    top.linkTo(parent.top, margin = (-25).dp)
                })
        }
        if (isHeader) {
            Text(
                text = hourLabel.format(DateTimeFormatter.ofPattern("hh:mm a")),
                color = LightBlue,
                modifier = Modifier.constrainAs(hours) {
                    centerTo(parent)
                    centerAround(parent.start)
                    end.linkTo(circle.start, timelinePadding.contentStart)
                })
        }
        if(textState == groupIndex) {
            Text(
                text = hourLabel.format(DateTimeFormatter.ofPattern("EEEE")),
                color = LightBlue,
                modifier = Modifier.constrainAs(day) {
                    centerTo(bottomLine)
                    centerAround(parent.start)
                    end.linkTo(topLine.end, margin = 65.dp)

                },
                fontWeight = FontWeight.Bold,
            )
        }


        if (popupVisible.value) {
            Popup(
                alignment = Alignment.CenterEnd,
                onDismissRequest = { popupVisible.value = false }) {
                Card(
                    modifier = Modifier.width(130.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White

                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                addDialogState.value = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        if (addDialogState.value) {
                            AddActivityDialog(
                                onAddEvent = onAddEvent,
                                state = addDialogState,
                                data = addDialogIndex
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Add Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                        Text(
                            text = "Delete Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_horizontal_split_24),
                                contentDescription = "Split",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Split Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_merge_24),
                                contentDescription = "Merge",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Merge Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        if (addSessionDialog.value) {
            AddSession(
                onAddSession = addSession,
                currentDate = hourLabel.toLocalDate(),
                state = addSessionDialog,
                data = addDialogIndex
            )
        }
        Icon(
            painter = painterResource(id = timelineOption.circleIcon),
            contentDescription = "circle",
            tint = if (isHeader) timelineOption.circleColor else Color.Transparent,
            modifier = Modifier
                .size(timelineOption.circleSize)
                .constrainAs(circle) {
                    start.linkTo(parent.start, margin = 70.dp)
                    top.linkTo(timelineContent.top)
                    bottom.linkTo(timelineContent.bottom)
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            addSessionDialog.value = true
                        },
                    )
                }
        )
        if (!isHeader) {
            Divider(
                modifier = Modifier.constrainAs(circleInnerLine) {
                    top.linkTo(circle.top)
                    bottom.linkTo(circle.bottom)
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    width = Dimension.value(timelineOption.lineWidth)
                    height = Dimension.fillToConstraints
                },
                color = timelineOption.lineColor
            )
        }


        Surface(
            modifier = Modifier
                .constrainAs(timelineContent) {
                    start.linkTo(circle.end, timelinePadding.contentStart)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            tapped = true
                            showToolTip = true
                            scope.launch {
                                val press = PressInteraction.Press(it)
                                interactionSource.emit(press)
                                interactionSource.emit(PressInteraction.Release(press))
                                delay(2000)
                                showToolTip = false
                                tapped = false
                            }
                        },
                        onLongPress = {
                            popupVisible.value = true
                        }
                    )
                }
                .indication(interactionSource, LocalIndication.current)
        ) {
            if (isHeader) {
                header(item)
            }
        }
        if (!(groupIndex == 0 && elementsIndex == HeaderIndex)) {
            Divider(
                modifier = Modifier.constrainAs(topLine) {
                    top.linkTo(parent.top)
                    bottom.linkTo(
                        circle.top,
                        if (isHeader) timelinePadding.circleLineGap else 0.dp
                    )
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    width = Dimension.value(timelineOption.lineWidth)
                    height = Dimension.fillToConstraints.atLeastWrapContent
                },
                color = timelineOption.lineColor
            )
        }

        if (!(groupIndex == groupSize - 1 && elementsIndex == elementsSize - 1)) {
            Divider(
                modifier = Modifier.constrainAs(bottomLine) {
                    top.linkTo(
                        circle.bottom,
                        if (isHeader) timelinePadding.circleLineGap else 0.dp
                    )
                    bottom.linkTo(parent.bottom)
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    width = Dimension.value(timelineOption.lineWidth)
                    height = Dimension.fillToConstraints
                },
                color = timelineOption.lineColor
            )
        }
    }
}





