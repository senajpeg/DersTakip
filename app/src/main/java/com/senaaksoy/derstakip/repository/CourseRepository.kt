package com.senaaksoy.derstakip.repository

import com.senaaksoy.derstakip.roomDb.Course
import com.senaaksoy.derstakip.roomDb.CourseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CourseRepository @Inject constructor(private val courseDao:CourseDao){
    suspend fun insert(course: Course)=courseDao.insert(course)
    suspend fun delete(course: Course)=courseDao.delete(course)
    suspend fun update(course: Course)=courseDao.update(course)

    fun allCourses():Flow<List<Course>> = courseDao.getAllCourses().flowOn(Dispatchers.IO).distinctUntilChanged()
}