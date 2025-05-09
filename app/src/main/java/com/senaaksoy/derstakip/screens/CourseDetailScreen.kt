package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.components.CourseTopAppBar

@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId : Int?
){


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Button( onClick = {},
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8177A7))) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
                Text(text = stringResource(R.string.not_ekle))
            }
        }
        Divider(
            color = Color(0xFFCDBBD0),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

    }

}