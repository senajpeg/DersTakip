package com.senaaksoy.derstakip.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.derstakip.repository.NoteRepository
import com.senaaksoy.derstakip.roomDb.Note
import com.senaaksoy.derstakip.timer.StudyTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepo: NoteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Note>>(emptyList())
    val uiState: StateFlow<List<Note>> = _uiState.asStateFlow()

    val studyTimer = StudyTimer()
    val timerDisplay = studyTimer.elapsedTime

    private val _timerState = MutableStateFlow(TimerState.INITIAL)//geçerli durumu izlerken kullanacağımız flow değişkeni
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    enum class TimerState { //bunu yapmamın sebbei zamanlayıcının geçerli durumunu izlemek
        INITIAL,
        RUNNING,
        PAUSED,
        RESET
    }

    fun getAllNotes() {
        viewModelScope.launch {
            noteRepo.getAllNotes().collect { notes ->
                _uiState.value = notes
            }
        }
    }


    fun getNotesForCourse(courseId: Int) {
        viewModelScope.launch {
            noteRepo.getNotesForCourse(courseId).collect { notes ->
                _uiState.value = notes
                Log.d("Kontrol", "Kalan not sayısı: ${notes.size}")

            }

        }
    }


    var inputTitle by mutableStateOf("")
        private set
    var inputNoteContent by mutableStateOf("")
        private set

    fun upDateTitle(title: String){
        inputTitle=title
    }
    fun upDateNoteContent(noteContent: String){
        inputNoteContent=noteContent
    }

    fun saveNote(courseId: Int, title: String, noteContent: String) {
        val currentTimeMillis = System.currentTimeMillis()
        val newNote = Note(
            courseId = courseId,
            title = title.uppercase(),
            noteContent = noteContent.lowercase(),
            durationMillis = timerDisplay.value,
            timestamp = currentTimeMillis
        )
        addNoteToDb(newNote)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepo.delete(note)
            getNotesForCourse(note.courseId)
        }
    }
    fun deleteNotesByCourseId(courseId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            noteRepo.deleteNotesByCourseId(courseId)
            onComplete()
        }
    }


    fun editNotes(id: Int, courseId: Int,title: String,noteContent: String){
        val updatedDurationMillis = timerDisplay.value

        val existingNote = _uiState.value.find { it.id == id }
        val timestamp = existingNote?.timestamp ?: System.currentTimeMillis()

        val newNotes=Note(id = id,
            title = title,
            noteContent = noteContent,
            courseId = courseId,
            durationMillis = updatedDurationMillis,
            timestamp = timestamp
        )
        updateNote(newNotes)
    }


    private fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepo.update(note)
        }
    }

    private fun addNoteToDb(note: Note) {
        viewModelScope.launch {
            noteRepo.insert(note)
        }
    }

    fun clearNotes() {
        inputTitle = ""
        inputNoteContent=""
    }

    fun startTimer() {
        studyTimer.start()
        _timerState.value = TimerState.RUNNING
    }

    fun pauseTimer() {
        studyTimer.pause()
        _timerState.value = TimerState.PAUSED
    }

    fun resetTimer() {
        studyTimer.reset()
        _timerState.value = TimerState.RESET
    }

    fun resumeTimer() {
        studyTimer.resume()
        _timerState.value = TimerState.RUNNING
    }
    fun setTimerValue(value: Long) { //zamanlayıcıyı belli bir değere ayarlıyo
        studyTimer.setElapsedTime(value)
        _timerState.value = TimerState.INITIAL
    }

    fun calculateTotalDuration(notes: List<Note>): Long {
        return notes.sumOf { (it.durationMillis / 1000) * 1000 }
    }
    override fun onCleared() {
        super.onCleared()
        studyTimer.pause()
    }
    fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(java.util.Date(timestamp))
    }


}
