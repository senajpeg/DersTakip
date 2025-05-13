package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.components.EditButton
import com.senaaksoy.derstakip.navigation.Screen
import com.senaaksoy.derstakip.roomDb.Note
import java.util.concurrent.TimeUnit

@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId: Int?,
    groupedNotes: Map<String, List<Note>>,
    clearItem: () -> Unit,
    resetTimer: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditButton(
            onClick = {
                clearItem()
                resetTimer()
                navController.navigate("${Screen.AddNoteScreen.route}/${courseId}") },
            isIconVisible = true,
            icon = Icons.Filled.Add,
            text = R.string.not_ekle
        )

        HorizontalDivider(
            color = Color(0xFFCDBBD0),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            groupedNotes.forEach { (title, notes) ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3CBDA))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge
                            )

                            notes.forEachIndexed { index, note ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp)
                                    ) {
                                        Text(
                                            text = "${index + 1}. ${note.noteContent}"
                                        )
                                        if (note.durationMillis > 0) {
                                            Text(
                                                text = "Süre: ${formatTime(note.durationMillis)}",
                                                modifier = Modifier.padding(start = 4.dp),
                                                color = Color(0xFF887A9D)
                                            )
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            navController.navigate("${Screen.EditNoteScreen.route}/${courseId}/${note.id}")
                                        }
                                    )
                                }

                                if (index < notes.size - 1) {
                                    HorizontalDivider(
                                        color = Color(0xFFCDBBD0),
                                        thickness = 0.5.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}