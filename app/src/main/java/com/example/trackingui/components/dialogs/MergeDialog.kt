package com.example.trackingui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.ui.theme.LightBlue

@Composable
fun MergeDialog(
    state: MutableState<Boolean>,
    onMerge: (id: String,index : Int) -> Unit,
    id: MutableState<ModeldataId>,
    totalCount: Int,
    listIndex : Int,
) {
    CommonDialog(state) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Add split count of $totalCount into this row?", color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onMerge(id.value.index,listIndex)
                        state.value = false
                    }, shape = RoundedCornerShape(5.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Merge")
                }

                Button(
                    onClick = { state.value = false },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }

}