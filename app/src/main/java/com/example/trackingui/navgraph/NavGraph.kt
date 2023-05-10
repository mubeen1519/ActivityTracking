package com.example.trackingui.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trackingui.screens.FitnessTimeline
import com.example.trackingui.screens.CategoryScreens
import com.example.trackingui.screens.MeditationScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController) {
NavHost(navController = navHostController, startDestination = CategoryScreens.FitnessScreen.route){
    composable(CategoryScreens.FitnessScreen.route){
        FitnessTimeline(navHostController)
    }
    composable(CategoryScreens.MeditationScreen.route){
        MeditationScreen(navController = navHostController)
    }
}
}