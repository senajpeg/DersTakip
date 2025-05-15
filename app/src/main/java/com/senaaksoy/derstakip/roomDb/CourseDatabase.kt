package com.senaaksoy.derstakip.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Course::class, Note::class], version = 4, exportSchema = false)
abstract class CourseDatabase : RoomDatabase(){
    abstract fun courseDao(): CourseDao
    abstract fun noteDao(): NoteDao
}