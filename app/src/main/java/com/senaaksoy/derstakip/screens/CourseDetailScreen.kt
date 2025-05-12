package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.navigation.Screen
import com.senaaksoy.derstakip.roomDb.Note

@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId: Int?,
    noteList: List<Note>
) {



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { navController.navigate("${Screen.AddNoteScreen.route}/${courseId}") },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8177A7))
        ) {

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

        LazyColumn {
            items(noteList) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD3CBDA))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = note.title,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = note.noteContent
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                navController.navigate("${Screen.EditNoteScreen.route}/${courseId}/${note.id}")
                            }
                        )

                    }

                }

            }
        }

    }

}

