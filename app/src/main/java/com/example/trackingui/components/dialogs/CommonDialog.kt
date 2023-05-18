package com.example.trackingui.components.dialogs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDialog(state : MutableState<Boolean>,content : @Composable () -> Unit){
    AlertDialog(onDismissRequest = { state.value = false }, content = content, modifier = Modifier.clip(
        RoundedCornerShape(10.dp)
    ))
}