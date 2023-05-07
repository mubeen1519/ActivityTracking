package com.example.trackingui.ui.theme

import kotlinx.coroutines.flow.StateFlow

enum class AppTheme{
    DAY,
    NIGHT,
    AUTO;

    companion object{
        fun fromOrdinal(ordinal : Int) = values()[ordinal]
    }
}


interface ThemeSetting {
    val themeFlow : StateFlow<AppTheme>
    var theme : AppTheme
}