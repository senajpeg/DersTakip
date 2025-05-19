package com.senaaksoy.derstakip.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.derstakip.repository.CourseRepository
import com.senaaksoy.derstakip.roomDb.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(private val courseRepo: CourseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Course>>(emptyList())
    val uiState: StateFlow<List<Course>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            courseRepo.allCourses().collect { courseList ->
                _uiState.value = courseList
            }
        }
    }

    var inputCourseName by mutableStateOf("")
        private set

    fun upDateCourseName(courseName: String) {
        inputCourseName = courseName

    }

    fun saveCourse(courseName: String) {
        val newItem = Course(
            name = courseName.uppercase()
        )
        addDb(newItem)
    }

    fun upDateCourse(course: Course) {
        val updatedCourse = course.copy(name = course.name.uppercase())
        viewModelScope.launch {
            courseRepo.update(updatedCourse)
        }
    }

    private fun addDb(course: Course) {
        viewModelScope.launch { courseRepo.insert(course) }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            courseRepo.delete(course)
        }
    }

    fun clearCourses() {
        inputCourseName = ""
    }

}