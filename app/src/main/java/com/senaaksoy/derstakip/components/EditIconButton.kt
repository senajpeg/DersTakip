package com.senaaksoy.derstakip.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EditIconButton(
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier,
        colors = ButtonColors(
            containerColor = Color(0xFFFEFDFF),
            contentColor = Color(0xFF8C799D),
            disabledContentColor =Color(0xFF434346),
            disabledContainerColor = Color(0xFF69686E)
        ),
        border = BorderStroke(1.dp,Color(0xFF8C799D))
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = text ?: ""
            )
        }
        text?.let {
            Text(text = it)
        }
    }
}
@Composable
fun EditButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFF8177A7))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(4.dp)
        )
    }
}