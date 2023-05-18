package com.example.trackingui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ZoomableLayout(content: @Composable () -> Unit) {
    val scale = remember { mutableStateOf(1f) }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale.value *= zoom
                    scale.value = scale.value.coerceIn(0.5f, 3f)
                }

            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale.value = 1f
                    }
                )
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
    ) {
        content()
    }
}