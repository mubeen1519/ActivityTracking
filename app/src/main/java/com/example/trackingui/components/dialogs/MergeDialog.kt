package com.example.trackingui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
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
                .background(Color.White)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Add split count of $totalCount into this row?")
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
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Merge")
                }

                Button(
                    onClick = { state.value = false },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }

}