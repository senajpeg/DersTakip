package com.senaaksoy.derstakip.viewModel

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
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepo: NoteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Note>>(emptyList())
    val uiState: StateFlow<List<Note>> = _uiState.asStateFlow()

    val studyTimer = StudyTimer()
    val timerDisplay = studyTimer.elapsedTime

    private val _timerState = MutableStateFlow(TimerState.INITIAL)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()



    fun getNotesForCourse(courseId: Int) {
        viewModelScope.launch {
            noteRepo.getNotesForCourse(courseId).collect { notes ->
                _uiState.value = notes
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
        val newNote = Note(
            courseId = courseId,
            title = title,
            noteContent = noteContent,
            durationMillis = timerDisplay.value
        )
        addNoteToDb(newNote)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepo.delete(note)
            getNotesForCourse(note.courseId)
        }
    }

    fun editNotes(id: Int, courseId: Int,title: String,noteContent: String){
        val updatedDurationMillis = timerDisplay.value

        val newNotes=Note(id = id,
            title = title,
            noteContent = noteContent,
            courseId = courseId,
            durationMillis = updatedDurationMillis
        )
        updateNote(newNotes)
    }


    fun updateNote(note: Note) {
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
    fun setTimerValue(value: Long) {
        studyTimer.setElapsedTime(value)
        if (value > 0) {
            _timerState.value = TimerState.INITIAL
        } else {
            _timerState.value = TimerState.INITIAL
        }
    }

    override fun onCleared() {
        super.onCleared()
        studyTimer.pause()
    }

    enum class TimerState {
        INITIAL,
        RUNNING,
        PAUSED,
        RESET
    }
}
