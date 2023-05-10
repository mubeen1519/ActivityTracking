package com.example.trackingui.screens

sealed class CategoryScreens (val route : String){
    object FitnessScreen : CategoryScreens("Walking")
    object MeditationScreen : CategoryScreens("Meditation")
}