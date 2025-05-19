package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.components.EditButton
import com.senaaksoy.derstakip.components.EditIconButton
import com.senaaksoy.derstakip.components.EditTextField
import com.senaaksoy.derstakip.roomDb.Note

@Composable
fun EditNoteScreen(
    navController: NavController,
    courseId: Int?,
    noteId: Int?,
    noteList: List<Note>,
    editNotes: (Int, Int, String, String) -> Unit,
    deleteNote: (Note) -> Unit,
    startTimer: () -> Unit,
    resetTimer: () -> Unit,
    resumeTimer: () -> Unit,
    pauseTimer: () -> Unit,
    formattedTime: String,
    timerState: String,
    setTimerValue: (Long) -> Unit
) {
    val note = noteList.firstOrNull { it.id == noteId }
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var noteContent by rememberSaveable { mutableStateOf(note?.noteContent ?: "") }

    val timerInitialized = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!timerInitialized.value && note != null) {
            setTimerValue(note.durationMillis)
            timerInitialized.value = true
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF887A9D))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = formattedTime,
                    modifier = Modifier.padding(24.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White

                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EditIconButton(
                icon = Icons.Filled.PlayArrow,
                onClick = {
                    note?.let { setTimerValue(it.durationMillis) }
                    startTimer()
                },
                isEnabled = timerState == "reset",
                modifier = Modifier.weight(1f)
            )
            EditIconButton(
                icon = Icons.Filled.RestartAlt,
                onClick = { resetTimer() },
                isEnabled = timerState == "paused",
                modifier = Modifier.weight(1f)
            )
            EditIconButton(
                icon = Icons.Filled.PlayCircle,
                onClick = { resumeTimer() },
                isEnabled = timerState == "initial" || timerState == "paused",
                modifier = Modifier.weight(1f)
            )
            EditIconButton(
                icon = Icons.Filled.Stop,
                onClick = { pauseTimer() },
                isEnabled = timerState == "running",
                modifier = Modifier.weight(1f)
            )

        }
        HorizontalDivider(
            color = Color(0xFFCDBBD0),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            EditTextField(
                value = title,
                onValueChange = { title = it },
                label = stringResource(R.string.konu_basligi)
            )
            EditTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                label = stringResource(R.string.calisma_detayları)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        EditButton(
            onClick = {
                editNotes(note!!.id, courseId!!, title, noteContent)
                navController.popBackStack()
            },
            text = R.string.guncelle,
            enabled = title.isNotBlank() && noteContent.isNotBlank(),
            isIconVisible = false
        )
        EditButton(
            onClick = {
                note?.let { deleteNote(it) }
                navController.popBackStack()
            },
            text = R.string.notu_sil,
            isIconVisible = false

        )
        EditButton(
            onClick = { navController.popBackStack() },
            text = R.string.iptal,
            isIconVisible = false
        )

    }
}