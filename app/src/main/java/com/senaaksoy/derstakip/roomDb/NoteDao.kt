package com.senaaksoy.derstakip.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE course_id = :courseId ORDER BY id ASC")
    fun getNotesForCourse(courseId: Int): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM notes WHERE course_id = :courseId")
    suspend fun deleteNotesByCourseId(courseId: Int)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Update
    suspend fun update(note: Note)
}
