package com.example.trackingui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.trackingui.screens.ActivityTimeline
import com.example.trackingui.ui.theme.AppTheme
import com.example.trackingui.ui.theme.ThemeSetting
import com.example.trackingui.ui.theme.TrackingUITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeSetting: ThemeSetting

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        setContent {

//            val theme = themeSetting.themeFlow.collectAsState()
//            val useDarkColors = when (theme.value) {
//                AppTheme.DAY -> true
//                AppTheme.AUTO -> isSystemInDarkTheme()
//                AppTheme.NIGHT -> false
//            }
            TrackingUITheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ActivityTimeline()
                }
            }
        }
    }
}

