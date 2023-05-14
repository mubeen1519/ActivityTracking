package com.example.trackingui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDialog(state : MutableState<Boolean>,content : @Composable () -> Unit){
    AlertDialog(onDismissRequest = { state.value = false }, content = content)
}