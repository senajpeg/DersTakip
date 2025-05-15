package com.senaaksoy.derstakip.repository

import com.senaaksoy.derstakip.roomDb.Note
import com.senaaksoy.derstakip.roomDb.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    suspend fun insert(note: Note) = noteDao.insert(note)
    suspend fun delete(note: Note) = noteDao.delete(note)
    suspend fun update(note: Note) = noteDao.update(note)

    fun getNotesForCourse(courseId: Int): Flow<List<Note>> =
        noteDao.getNotesForCourse(courseId).flowOn(Dispatchers.IO).distinctUntilChanged()

    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun deleteNotesByCourseId(courseId: Int) {
        noteDao.deleteNotesByCourseId(courseId)
    }
}
