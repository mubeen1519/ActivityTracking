package com.example.trackingui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingui.ui.theme.ThemeSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    @Inject
    lateinit var themeSetting: ThemeSetting

    @RequiresApi(Build.VERSION_CODES.O)
    var events = mutableStateOf(TimelineEvent())

    @RequiresApi(Build.VERSION_CODES.O)
    fun addActivity(event : TimelineEvent, newValue : Int){
        viewModelScope.launch {
           val newActivity = events.value.copy(maxCount = event.maxCount.plus(newValue))
        }


    }
}