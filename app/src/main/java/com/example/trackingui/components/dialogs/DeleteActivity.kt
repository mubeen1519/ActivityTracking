package com.example.trackingui.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trackingui.model.ModeldataId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteActivityDialog(
    onDelete: (index: String) -> Unit,
    state: MutableState<Boolean>,
    id: MutableState<ModeldataId>
) {
    AlertDialog(onDismissRequest = { state.value = false }) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {
            Text(
                text = "Are you sure? you want to delete activity",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onDelete(id.value.index)
                        state.value = false
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Red
                    )
                ) {
                    Text(text = "Delete")
                }

                Button(
                    onClick = { state.value = false },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }

    }
}