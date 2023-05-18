package com.example.trackingui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.trackingui.ui.theme.ThemeSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    @Inject
    lateinit var themeSetting: ThemeSetting

//    val _timelineEvents = MutableLiveData<List<TimelineEvent>>()
//    val timelineEvents: LiveData<List<TimelineEvent>> = _timelineEvents
//
//    var lastItem: MutableState<ActivityTypeAndCount?> = mutableStateOf(null)
//
//    val hour = kotlin.random.Random.nextInt(0, 24)
//    val minute = kotlin.random.Random.nextInt(0, 60)
//    @RequiresApi(Build.VERSION_CODES.O)
//    val hours = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute))
//
//    val list: MutableList<ActivityTypeAndCount> = mutableListOf()
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadInitialData() {
//        // Load initial data here, e.g., from a repository or API
//        val initialData: List<TimelineEvent> = listOf(
//            TimelineEvent(
//                list = list
//            )
//        )
//            _timelineEvents.value = initialData
//    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadData(activityCategory: ActivityCategory){
//        viewModelScope.launch {
//            val newData = loadMoreData(LocalDate.now(), activityCategory)
//            val updatedData = _timelineEvents.value.orEmpty() + newData
//            _timelineEvents.value = updatedData
//        }
//    }
//
//    fun addActivity(id : String, count : Int,activity : ActivityCategory,listIndex : Int){
//        viewModelScope.launch {
//            val newEvent = timelineEvents.value?.toMutableList()
//            val eventIndex = newEvent?.find { it.id == id }
//            val activityIndex = eventIndex?.list?.find { it.activity == activity }
//            if (activityIndex != null) {
//                activityIndex.count += count
//            } else {
//                eventIndex?.list?.add(
//                    listIndex,
//                    ActivityTypeAndCount(
//                        count = count,
//                        activity = activity
//                    )
//                )
//            }
//            _timelineEvents.value = newEvent!!
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun addSession(timelineEvent: TimelineEvent, id: String){
//        viewModelScope.launch {
//            val session = timelineEvents.value?.toMutableList()
//            val newIndex = timelineEvents.value?.find { it.id == id }
//            val checktime = newIndex?.hours?.toLocalTime()
//            if (checktime != null) {
//                if (checktime > timelineEvent.hours.toLocalTime())
//                    session?.add(timelineEvent)
//            }
//            _timelineEvents.value = session!!
//        }
//    }
//
//    fun onDeleteActivity(id : String, listIndex: Int){
//        viewModelScope.launch {
//            val newEvent = timelineEvents.value?.toMutableList()
//            val eventIndex = newEvent?.find { it.id == id }
//            eventIndex?.list?.removeAt(listIndex)
//            _timelineEvents.value = newEvent!!
//        }
//    }
//
//    fun onDeleteSession(id : String){
//        viewModelScope.launch {
//            val newEvent = timelineEvents.value?.toMutableList()
//            val newIndex = newEvent?.find { it.id == id }
//            newEvent?.remove(newIndex)
//            _timelineEvents.value = newEvent!!
//        }
//    }
//
//    fun onSplitActivity(id: String,listIndex: Int,context: Context){
//        viewModelScope.launch {
//            val event = timelineEvents.value?.toMutableList()
//            val i = event?.find { it.id == id }
//            val activityIndex = i?.list?.get(listIndex)
//            if (activityIndex?.count == 0) {
//                Toast.makeText(
//                    context,
//                    "Sorry there is no activity present in here",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                val (firstPair, secondPair) = splitInt(activityIndex?.count!!)
//                activityIndex.count = firstPair
//                lastItem.value = activityIndex
//                val remainingItem = activityIndex.copy(count = secondPair)
//                i.list[listIndex] = remainingItem
//                _timelineEvents.value = event!!
//            }
//        }
//    }
//
//    fun onMergeActivity(id: String, listIndex: Int){
//        val event = timelineEvents.value?.toMutableList()
//        val i = event?.find { it.id == id }
//        val activityIndex = i?.list?.get(listIndex)
//        if (activityIndex != null) {
//            i.list.add(listIndex, lastItem.value!!)
//        }
//        _timelineEvents.value = event!!
//    }
}