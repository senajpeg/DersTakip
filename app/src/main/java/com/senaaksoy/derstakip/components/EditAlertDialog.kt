package com.senaaksoy.derstakip.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun EditAlertDialog(
    title: String,
    value: String,
    @StringRes text: Int,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String,
    isConfirmEnabled: Boolean = true,
    isTextFieldVisible:Boolean=true
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = isConfirmEnabled
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(dismissButtonText)
            }
        },
        title = { Text(title) },
        text = {
            if(isTextFieldVisible){
                EditTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = title
                )
            }else{
                Text(stringResource(text))
            }

        }
    )
}