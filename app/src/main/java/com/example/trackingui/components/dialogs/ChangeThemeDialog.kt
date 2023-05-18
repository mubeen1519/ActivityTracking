package com.example.trackingui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trackingui.components.buttons.RadioGroup
import com.example.trackingui.components.buttons.RadioItems
import com.example.trackingui.ui.theme.AppTheme
import com.example.trackingui.ui.theme.ThemeSetting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeThemeDialog(state : MutableState<Boolean>, userSetting : ThemeSetting){
    val theme = userSetting.themeFlow.collectAsState()
    AlertDialog(onDismissRequest = { state.value = false }) {
        ThemeContent(selectedTheme = theme.value, onItemSelect = {themes ->
            userSetting.theme = themes
        })
    }
    
}
@Composable
private fun ThemeContent(
    selectedTheme: AppTheme,
    onItemSelect: (AppTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    val themeItems = listOf(
        RadioItems(
            id = AppTheme.DAY.ordinal,
            name = "Night Theme"
        ),
        RadioItems(
            id = AppTheme.NIGHT.ordinal,
            name = "Light Theme"
        ),
        RadioItems(
            id = AppTheme.AUTO.ordinal,
            name = "Auto"
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Choose Your Theme", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(10.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
        Spacer(modifier = Modifier.height(10.dp))
        RadioGroup(
            items = themeItems,
            selected = selectedTheme.ordinal,
            onItemSelected = { id -> onItemSelect(AppTheme.fromOrdinal(id)) },
        )
    }
}