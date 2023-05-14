package com.example.trackingui.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingui.components.dialogs.splitInt
import com.example.trackingui.components.infinitescroll.loadMoreData
import com.example.trackingui.model.ActivityCategory
import com.example.trackingui.model.ActivityTypeAndCount
import com.example.trackingui.model.TimelineEvent
import com.example.trackingui.ui.theme.ThemeSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    @Inject
    lateinit var themeSetting: ThemeSetting

    private val _timelineEvents = MutableLiveData<List<TimelineEvent>>()
    val timelineEvents: LiveData<List<TimelineEvent>> = _timelineEvents

    var lastItem: MutableState<ActivityTypeAndCount?> = mutableStateOf(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(activityCategory: ActivityCategory){
        viewModelScope.launch {
            val newData = if (timelineEvents.value?.isEmpty() == true) {
                loadMoreData(LocalDate.now(), activityCategory)
            } else {
                loadMoreData(timelineEvents.value?.last()?.hours?.toLocalDate() ?: LocalDate.now(), activityCategory)
            }
            _timelineEvents.value = (_timelineEvents.value ?: emptyList()) + newData
        }
    }

    fun addActivity(id : String, count : Int,activity : ActivityCategory,listIndex : Int){
        viewModelScope.launch {
            val newEvent = timelineEvents.value?.toMutableList()
            val eventIndex = newEvent?.find { it.id == id }
            val activityIndex = eventIndex?.list?.find { it.activity == activity }
            if (activityIndex != null) {
                activityIndex.count += count
            } else {
                eventIndex?.list?.add(
                    listIndex,
                    ActivityTypeAndCount(
                        count = count,
                        activity = activity
                    )
                )
            }
            _timelineEvents.value = newEvent!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSession(timelineEvent: TimelineEvent, id: String){
        viewModelScope.launch {
            val session = timelineEvents.value?.toMutableList()
            val newIndex = timelineEvents.value?.find { it.id == id }
            val checktime = newIndex?.hours?.toLocalTime()
            if (checktime != null) {
                if (checktime > timelineEvent.hours.toLocalTime())
                    session?.add(timelineEvent)
            }
            _timelineEvents.value = session!!
        }
    }

    fun onDeleteActivity(id : String, listIndex: Int){
        viewModelScope.launch {
            val newEvent = timelineEvents.value?.toMutableList()
            val eventIndex = newEvent?.find { it.id == id }
            eventIndex?.list?.removeAt(listIndex)
            _timelineEvents.value = newEvent!!
        }
    }

    fun onDeleteSession(id : String){
        viewModelScope.launch {
            val newEvent = timelineEvents.value?.toMutableList()
            val newIndex = newEvent?.find { it.id == id }
            newEvent?.remove(newIndex)
            _timelineEvents.value = newEvent!!
        }
    }

    fun onSplitActivity(id: String,listIndex: Int,context: Context){
        viewModelScope.launch {
            val event = timelineEvents.value?.toMutableList()
            val i = event?.find { it.id == id }
            val activityIndex = i?.list?.get(listIndex)
            if (activityIndex?.count == 0) {
                Toast.makeText(
                    context,
                    "Sorry there is no activity present in here",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val (firstPair, secondPair) = splitInt(activityIndex?.count!!)
                activityIndex.count -= secondPair
                lastItem.value = activityIndex
                i.list[listIndex] = activityIndex
                _timelineEvents.value = event!!
            }
        }
    }

    fun onMergeActivity(id: String, listIndex: Int){
        val event = timelineEvents.value?.toMutableList()
        val i = event?.find { it.id == id }
        val activityIndex = i?.list?.get(listIndex)
        if (activityIndex != null) {
            i.list.add(listIndex, lastItem.value!!)
        }
        _timelineEvents.value = event!!
    }
}