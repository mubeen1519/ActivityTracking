package com.example.trackingui.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackingui.model.ModeldataId
import com.example.trackingui.model.TimelineEvent
import kotlin.math.ceil

fun splitInt(value: Int): Pair<Int, Int> {
    val firstPart = value / 2
    val secondPart = value - firstPart
    return Pair(firstPart, secondPart)
}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  SplitDialog(
    state: MutableState<Boolean>,
    onSplit: (id: String, index: Int) -> Unit,
    id: MutableState<ModeldataId>,
    event: TimelineEvent,
    index: Int,
) {

    CommonDialog(state = state) {
        val (firstPair, secondPair) = splitInt(event.list[index].count)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Activity has cycles  ${event.list[index].count} of ${event.list[index].activity} into $firstPair + $secondPair ",
                fontSize = 13.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    onSplit(id.value.index, index)
                    state.value = false
                }, shape = RoundedCornerShape(5.dp), modifier = Modifier.height(40.dp)) {
                    Text(text = "Split", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { state.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Cancel")
                }
            }

        }
    }
}
