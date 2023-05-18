package com.example.trackingui.components.timeline

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeastWrapContent
import com.example.trackingui.R
import com.example.trackingui.components.buttons.ScheduleHeader
import com.example.trackingui.components.dialogs.*
import com.example.trackingui.components.infinitescroll.loadMoreData
import com.example.trackingui.components.shape.Bubble
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.model.TimelineEvent
import com.example.trackingui.ui.theme.LightBlue
import com.example.trackingui.ui.theme.OceanBlue
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
    item: List<TimelineEvent>,
    modifier: Modifier = Modifier,
    timelineOption: TimeLineOption = TimeLineOption(),
    timelinePadding: TimeLinePadding = TimeLinePadding(),
    header: @Composable (TimelineEvent, onItemClicked: (Int) -> Unit, onItemLongClick: (Int) -> Unit) -> Unit,
    activityCategory: ActivityCategory,
) {
    val context = LocalContext.current
    val currentDate : MutableState<LocalDate> =  remember { mutableStateOf(LocalDate.now()) }


    var allData by remember { mutableStateOf(item) }
    val scope = rememberCoroutineScope()
    var lastItem: ActivityTypeAndCount? by remember {
        mutableStateOf(null)
    }

    fun updateCurrentDate(newDate: LocalDate) {
        scope.launch {
            currentDate.value = newDate
        }
    }
    val findTargetIndex: (List<TimelineEvent>, LocalDate) -> Int = { data, targetDate ->
         data.indexOfFirst { it.hours.toLocalDate() == targetDate }
    }



    val listState = rememberLazyListState()

    val loadData: () -> Unit = {
        val newData =
            if (allData.isEmpty()) loadMoreData(
                LocalDate.now(),
                activityCategory
            ) else loadMoreData(
                allData[allData.size - 1].hours.toLocalDate(),
                activityCategory
            )
        allData = allData + newData
    }


    Column(modifier = Modifier.padding(top = 60.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ScheduleHeader(
                dateTime = currentDate,
                allData = allData,
                listState = listState,
                scope = scope,
                updateCurrentDate = {
                    currentDate.value = it
                },
                findTargetIndex = findTargetIndex,
            )
        }


        LazyColumn(
            state = listState,
            modifier = modifier,
            userScrollEnabled = true,
            contentPadding = timelinePadding.defaultPadding,
        ) {
            val sortedData = allData.stream()
                .sorted(Comparator.comparing(TimelineEvent::hours, reverseOrder())).toList()
            sortedData.forEachIndexed { index, value ->
                stickyHeader {
                    TimelineView(
                        itemId = value.id,
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
                        onInsertEvent = { i, count, activity, listIndex ->
                            val newEvent = allData.toMutableList()
                            val eventIndex = newEvent.find { it.id == i }
                            eventIndex?.list?.add(
                                listIndex,
                                ActivityTypeAndCount(
                                    count = count,
                                    activity = activity,
                                    metricPercentage = (count.toDouble() / 10) * 100
                                )
                            )
                            allData = newEvent
                        },
                        addSession = { timelineEvent, id ->
                            val session = allData.toMutableList()
                            val newIndex = session.find { it.id == id }
                            val checktime = newIndex?.hours?.toLocalTime()
                            if (checktime != null) {
                                if (checktime > timelineEvent.hours.toLocalTime())
                                    session.add(timelineEvent)
                            }
                            allData = session

                        },
                        onDelete = { id, listIndex ->
                            val newEvent = allData.toMutableList()
                            val eventIndex = newEvent.find { it.id == id }
                            eventIndex?.list?.removeAt(listIndex)
                            allData = newEvent
                        },
                        activityType = activityCategory,
                        onDeleteSession = { _, id ->
                            val newEvent = allData.toMutableList()
                            val newIndex = newEvent.find { it.id == id }
                            newEvent.remove(newIndex)
                            allData = newEvent
                        },
                        onSplit = { id, listindex, splitCount ->
                            val event = allData.toMutableList()
                            val i = event.find { it.id == id }
                            val activityIndex = i?.list?.get(listindex)
                            if (activityIndex?.count == 0) {
                                Toast.makeText(
                                    context,
                                    "Sorry there is no activity present in here",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                if (activityIndex != null) {
                                    if (splitCount > activityIndex.count) {
                                        Toast.makeText(
                                            context,
                                            "Sorry total count is less than split-count",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        activityIndex.count = activityIndex.count.minus(splitCount)
                                        lastItem = activityIndex.copy(splitCount)
                                        activityIndex.metricPercentage =
                                            (activityIndex.count.toDouble() / 10) * 100
                                        i.list[listindex] = activityIndex
                                        allData = event
                                    }
                                }


                            }
                        },
                        onMerge = { id, listIndex ->
                            val event = allData.toMutableList()
                            val i = event.find { it.id == id }
                            val activityIndex = i?.list?.get(listIndex)
                            if (lastItem == null || activityIndex == null) {
                                Toast.makeText(
                                    context,
                                    "You can't merge empty activity,sorry!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                activityIndex.metricPercentage =
                                    (lastItem!!.count.toDouble() / 10) * 100
                                i.list.add(listIndex, lastItem!!)
                                lastItem = null
                            }
                            allData = event
                        },
                        onEdit = { id, listIndex, activity, count ->
                            val newEvent = allData.toMutableList()
                            val eventIndex = newEvent.find { it.id == id }
                            val activityIndex = eventIndex?.list?.get(listIndex)
                            activityIndex?.count = count
                            activityIndex?.activity = activity
                            activityIndex?.metricPercentage = (count.toDouble() / 10) * 100
                            allData = newEvent
                        },
                        onGroup = { id, listIndex ->
                            val event = allData.toMutableList()
                            val eventIndex = event.indexOfFirst { it.id == id }
                            if (eventIndex != -1) {
                                val clickedActivity = event[eventIndex].list[listIndex]
                                val previousActivity =
                                    if (listIndex > 0) event[eventIndex].list[listIndex - 1] else null
                                val nextActivity =
                                    if (listIndex < event[eventIndex].list.size - 1) event[eventIndex].list[listIndex + 1] else null
                                if (previousActivity?.activity?.getName() == clickedActivity.activity.getName()) {
                                    clickedActivity.count += previousActivity.count
                                    clickedActivity.metricPercentage =
                                        (clickedActivity.count.toDouble() / 10) * 100
                                    event[eventIndex].list.removeAt(listIndex - 1)
                                } else if (nextActivity?.activity?.getName() == clickedActivity.activity.getName()) {
                                    clickedActivity.count += nextActivity.count
                                    clickedActivity.metricPercentage =
                                        (clickedActivity.count.toDouble() / 10) * 100
                                    event[eventIndex].list.removeAt(listIndex + 1)

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Sorry no matching activity found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        onAddActivity = { id, count, activity ->
                            val newEvent = allData.toMutableList()
                            val eventIndex = newEvent.find { it.id == id }
                            eventIndex?.list?.add(
                                ActivityTypeAndCount(
                                    count = count,
                                    activity = activity,
                                    metricPercentage = (count.toDouble() / 10) * 100
                                )
                            )
                            allData = newEvent
                        },
                        lastItem = lastItem,
                        listState = listState,
                    )
                }
            }
        }
    }



    LaunchedEffect(Unit) {
        if (allData.isNotEmpty()) {
            currentDate.value = allData[0].hours.toLocalDate()
        }
    }


    listState.ScrolledToEnd(
        loadMore = {
            loadData()
        },
        setHeaderDate = {
            if (allData.size > it) {
                updateCurrentDate(allData[it].copy().hours.toLocalDate())

            }
        }
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun LazyListState.ScrolledToEnd(
    loadMore: () -> Unit,
    setHeaderDate: (Int) -> Unit,
    bufferCount: Int = 1,
) {

    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = (layoutInfo.visibleItemsInfo.lastOrNull()
                ?.index ?: 0) + 1
            setHeaderDate(lastVisibleItem)
            lastVisibleItem > (totalItems - bufferCount)
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TimelineView(
    item: TimelineEvent,
    hourLabel: LocalDateTime,
    groupSize: Int,
    groupIndex: Int,
    itemId: String,
    lastItem: ActivityTypeAndCount?,
    elementsSize: Int,
    elementsIndex: Int,
    listState: LazyListState,
    timelineOption: TimeLineOption,
    timelinePadding: TimeLinePadding,
    isHeader: Boolean,
    header: @Composable (TimelineEvent, onItemClicked: (Int) -> Unit, onItemLongClick: (Int) -> Unit) -> Unit,
    onInsertEvent: (id: String, maxCount: Int, activity: ActivityCategory, listIndex: Int) -> Unit,
    onAddActivity: (id: String, maxCount: Int, activity: ActivityCategory) -> Unit,
    activityType: ActivityCategory,
    addSession: (TimelineEvent, index: String) -> Unit,
    onSplit: (id: String, listIndex: Int, splitCount: Int) -> Unit,
    onMerge: (id: String, listIndex: Int) -> Unit,
    onDelete: (index: String, listIndex: Int) -> Unit,
    onDeleteSession: (TimelineEvent, index: String) -> Unit,
    onEdit: (id: String, listIndex: Int, activity: ActivityCategory, count: Int) -> Unit,
    onGroup: (id: String, listIndex: Int) -> Unit,
) {

    val textState by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    val eventId: MutableState<ModeldataId> = remember {
        mutableStateOf(
            ModeldataId(
                index = itemId,
            )
        )
    }
    var showToolTip by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember { MutableInteractionSource() }

    val insertDialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val addActivityDialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val addSessionDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val deleteDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val splitDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val mergeDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val popupDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val popupSecondDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val editDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val groupDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    val selectedIndex = remember {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope()


    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(timelineOption.contentHeight)
    )
    {
        val (circle, circleInnerLine, topLine, bottomLine, timelineContent, hours, bubbleC, day) = createRefs()

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
        if (textState == groupIndex) {
            Text(
                text = hourLabel.format(DateTimeFormatter.ofPattern("EEEE")),
                color = OceanBlue,
                modifier = Modifier.constrainAs(day) {
                    centerTo(bottomLine)
                    centerAround(parent.start)
                    end.linkTo(topLine.end, margin = 65.dp)

                },
                fontWeight = FontWeight.Bold,
            )
        }

        if (showToolTip) {
            Bubble(
                date = hourLabel,
                events = item,
                modifier = Modifier.constrainAs(bubbleC) {
                    start.linkTo(circle.end)
                    top.linkTo(parent.top, margin = (-25).dp)
                },
                index = selectedIndex.value
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
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = {
                            scope.launch {
                                val press = PressInteraction.Press(it)
                                interactionSource.emit(press)
                                interactionSource.emit(PressInteraction.Release(press))
                                delay(1000)
                            }
                        },
                        onLongPress = {
                            popupSecondDialog.value = true
                            scope.launch {
                                val press = PressInteraction.Press(it)
                                interactionSource.emit(press)
                                interactionSource.emit(PressInteraction.Release(press))
                                delay(1000)


                            }
                        }
                    )
                }
                .indication(interactionSource, LocalIndication.current)
        ) {
            if (isHeader) {
                header(item, onItemClicked = {
                    selectedIndex.value = it
                    showToolTip = true
                    scope.launch {
                        delay(2000)
                        showToolTip = false
                    }
                }, onItemLongClick = {
                    selectedIndex.value = it
                    popupDialog.value = true
                })
            }
        }
        if (insertDialogState.value) {
            InsertActivityDialog(
                onInsertEvent = onInsertEvent,
                state = insertDialogState,
                id = eventId,
                activityEnumClass = activityType,
                listIndex = selectedIndex.value
            )
        }

        if (deleteDialog.value) {
            DeleteActivityDialog(
                onDelete = onDelete,
                state = deleteDialog,
                id = eventId,
                listIndex = selectedIndex.value
            )
        }
        if (splitDialog.value) {
            SplitDialog(
                state = splitDialog,
                onSplit = onSplit,
                id = eventId,
                event = item,
                index = selectedIndex.value
            )
        }

        if (mergeDialog.value) {
            MergeDialog(
                state = mergeDialog,
                onMerge = onMerge,
                id = eventId,
                totalCount = lastItem!!.count,
                listIndex = selectedIndex.value
            )
        }

        if (editDialog.value) {
            EditActivityDialog(
                onEdit = onEdit,
                id = eventId,
                state = editDialog,
                activityEnumClass = activityType,
                listIndex = selectedIndex.value,
                oldCount = item.list[selectedIndex.value].count
            )
        }

        if (groupDialog.value) {
            GroupDialog(
                onGroup = onGroup,
                listIndex = selectedIndex.value,
                id = eventId,
                state = groupDialog,
                event = item
            )
        }

        if (addActivityDialogState.value) {
            AddActivityDialog(
                onAddActivity = onAddActivity,
                id = eventId,
                activityEnumClass = activityType,
                state = addActivityDialogState
            )
        }

        if (popupDialog.value) {
            Popup(
                alignment = Alignment.CenterEnd,
                onDismissRequest = { popupDialog.value = false },
                properties = PopupProperties(
                    focusable = true,
                    clippingEnabled = false
                )

                ) {
                Card(
                    modifier = Modifier.width(130.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background

                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                insertDialogState.value = true
                                popupDialog.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {

                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Insert Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                editDialog.value = true
                                popupDialog.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                contentDescription = "Edit",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Edit Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                deleteDialog.value = true
                                popupDialog.value = false
                            },
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
                            .padding(4.dp)
                            .clickable {
                                splitDialog.value = true
                                popupDialog.value = false
                            },
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
                    if(lastItem != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    mergeDialog.value = true
                                    popupDialog.value = false
                                },
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                groupDialog.value = true
                                popupDialog.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.tab_group_fill0_wght400_grad0_opsz48),
                                contentDescription = "group",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Group Activity",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            }
        }
        if (popupSecondDialog.value) {
            Popup(
                alignment = Alignment.CenterEnd,
                onDismissRequest = { popupSecondDialog.value = false },
                properties = PopupProperties(
                    focusable = true,
                    clippingEnabled = false
                )
            ) {
                Card(
                    modifier = Modifier.width(130.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                addActivityDialogState.value = true
                                popupSecondDialog.value = false
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add new",
                                tint = Color.Gray
                            )
                        }
                        Text(text = "Add Activity", color = MaterialTheme.colorScheme.onSurface)
                    }
                }

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
        if (addSessionDialog.value) {
            AddSession(
                onAddSession = addSession,
                currentDate = hourLabel.toLocalDate(),
                state = addSessionDialog,
                data = eventId,
                activityCategory = activityType,
                onDeleteSession = onDeleteSession
            )
        }

        if (!(groupIndex == groupSize - 1 && elementsIndex == elementsSize - 1)) {
            Divider(
                modifier = Modifier
                    .constrainAs(bottomLine) {
                        top.linkTo(
                            circle.bottom,
                            if (isHeader) timelinePadding.circleLineGap else 0.dp
                        )
                        bottom.linkTo(parent.bottom)
                        start.linkTo(circle.start)
                        end.linkTo(circle.end)
                        width = Dimension.value(timelineOption.lineWidth)
                        height = Dimension.fillToConstraints
                    }
                    .pointerInput(true) {
                        detectTapGestures(
                            onLongPress = {
                                addSessionDialog.value = true
                                scope.launch {
                                    val press = PressInteraction.Press(it)
                                    interactionSource.emit(press)
                                    interactionSource.emit(PressInteraction.Release(press))
                                    delay(2000)
                                }
                            }
                        )
                    }
                    .indication(interactionSource, LocalIndication.current),
                color = timelineOption.lineColor
            )
        }
    }
}






