package com.senaaksoy.derstakip.di

import android.content.Context
import androidx.room.Room
import com.senaaksoy.derstakip.roomDb.CourseDao
import com.senaaksoy.derstakip.roomDb.CourseDatabase
import com.senaaksoy.derstakip.roomDb.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun providesCourseDao(courseDatabase: CourseDatabase):CourseDao=courseDatabase.courseDao()

    @Singleton
    @Provides
    fun providesNoteDao(courseDatabase: CourseDatabase): NoteDao = courseDatabase.noteDao()

    @Singleton
    @Provides
    fun providesCourseDatabase(@ApplicationContext context : Context):CourseDatabase=
        Room.databaseBuilder(
            context,
            CourseDatabase::class.java,
            name="courses",
        ).fallbackToDestructiveMigration().build()
}