package com.senaaksoy.derstakip.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.derstakip.repository.NoteRepository
import com.senaaksoy.derstakip.roomDb.Note
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
            noteContent = noteContent
        )
        addNoteToDb(newNote)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepo.delete(note)
        }
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
}
