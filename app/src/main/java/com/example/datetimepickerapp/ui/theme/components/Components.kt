package com.example.datetimepickerapp.ui.theme.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.datetimepickerapp.getScreenWidth

@Composable
fun CustomAlertDialog(
    isOpened: Boolean,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String
) {

    val width = getScreenWidth()*0.9
    if (isOpened) {

        AlertDialog(modifier = Modifier.width(width.dp), shape = RoundedCornerShape(20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                onClose()
            },

            title = {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Text(
                    text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(40.dp),
                    onClick = {
                        onConfirm()
                    }) {
                    Text("Accept")
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(40.dp),
                    onClick = {
                        onClose()
                    }) {
                    Text("Cancel")
                }
            }
        )
    }
}

