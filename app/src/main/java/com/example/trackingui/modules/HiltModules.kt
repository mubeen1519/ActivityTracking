package com.example.trackingui.modules

import androidx.compose.ui.window.PopupPositionProvider
import com.example.trackingui.screens.ThemeSettingImpl
import com.example.trackingui.ui.theme.ThemeSetting
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HiltModules {

    @Binds
    @Singleton
    abstract fun bindThemeSetting(
        themeSettingImpl: ThemeSettingImpl
    ) : ThemeSetting


}